package hu.zsof.restaurantapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.adapter.ListAdapter
import hu.zsof.restaurantapp.databinding.ListFragmentBinding
import hu.zsof.restaurantapp.network.model.CustomFilter
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.util.Constants
import hu.zsof.restaurantapp.util.Constants.USER_DATA
import hu.zsof.restaurantapp.util.extensions.safeNavigate
import hu.zsof.restaurantapp.util.listeners.FavBtnClickListener
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)

        recyclerView = binding.recyclerRestaurantList

        // Get user's favIdsList to compare the full list -> if both contains the place, fill the fav icon
        val userJson = viewModel.getAppPreference<String>(USER_DATA)
        val user = Gson().fromJson(userJson, User::class.java)
        val userFavIdsList = user.favPlaceIds

        listAdapter = ListAdapter(
            object : FavBtnClickListener {
                override fun onFavBtnClicked(placeId: Long) {
                    lifecycleScope.launch {
                        val user = viewModel.addOrRemoveFavPlace(placeId)

                        // Refresh user's favIdList after add or remove
                        val userJson = Gson().toJson(user)
                        viewModel.setAppPreference(USER_DATA, userJson)
                    }
                }
            },
            userFavIdsList
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBindings()
    }

    private fun setupBindings() {
        binding.apply {
            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        listAdapter.filter.filter(newText)
                        return true
                    }
                }
            )

            filterBtn.setOnClickListener {
                safeNavigate(ListFragmentDirections.actionListFrToFilterDialogFr())
            }
            addNewPlaceBtn.setOnClickListener {
                safeNavigate(ListFragmentDirections.actionListFrToMapFr())
            }

            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
                Constants.FILTERED_ITEMS
            )?.observe(viewLifecycleOwner) {
                val filteredItemsJson = Gson().fromJson(it, CustomFilter::class.java)

                listAdapter.setCustomFilters(filteredItemsJson)
                binding.clearFiltersText.visibility = View.VISIBLE
            }

            binding.clearFiltersText.setOnClickListener {
                // todo nem törlődik a filterezés / nem adja vissza az egész listát
                listAdapter.resetFilters()
                binding.clearFiltersText.visibility = View.GONE
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.requestPlaceData()
        viewModel.places.observe(viewLifecycleOwner) {
            listAdapter.restaurantList = it
            binding.recyclerRestaurantList.adapter = listAdapter
        }
    }

    /*override fun onDialogClosed() {
        viewModel.requestPlaceData()
    }*/
}
