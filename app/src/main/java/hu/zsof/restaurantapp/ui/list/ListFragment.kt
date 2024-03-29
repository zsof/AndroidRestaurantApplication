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
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.adapter.ListAdapter
import hu.zsof.restaurantapp.databinding.ListFragmentBinding
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.util.Constants
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
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)

        /* val inflater = TransitionInflater.from(requireContext())
         enterTransition = inflater.inflateTransition(R.transition.slide_right)
 */
        recyclerView = binding.recyclerRestaurantList

        // Get user's favIdsList to compare the full list -> if both contains the place, fill the fav icon
        val userJson = viewModel.getAppPreference<String>(Constants.Prefs.USER_DATA)
        val user = Gson().fromJson(userJson, User::class.java)
        val userFavIdsList = user.favPlaceIds

        listAdapter = ListAdapter(
            object : FavBtnClickListener {
                override fun onFavBtnClicked(placeId: Long) {
                    lifecycleScope.launch {
                        val userData = viewModel.addOrRemoveFavPlace(placeId)

                        // Refresh user's favIdList after add or remove
                        val userJson = Gson().toJson(userData)
                        viewModel.setAppPreference(Constants.Prefs.USER_DATA, userJson)
                    }
                }
            },
            userFavIdsList,
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
                },
            )

            addNewPlaceBtn.setOnClickListener {
                safeNavigate(ListFragmentDirections.actionListFrToMapFr())
            }

            filterBtn.setOnClickListener {
                safeNavigate(ListFragmentDirections.actionListFrToFilterDialogFr())
            }

            // Get items from FilterDialogFr and set the filter method
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
                Constants.FILTERED_PLACES,
            )?.observe(viewLifecycleOwner) {
                val itemType = object : TypeToken<List<Place>>() {}.type
                val filteredPlaces: List<Place> = Gson().fromJson(it, itemType)

                listAdapter.setCustomFilters(filteredPlaces)
                binding.clearFiltersText.visibility = View.VISIBLE
            }

            binding.clearFiltersText.setOnClickListener {
                viewModel.requestPlaceData()
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
}
