package hu.zsof.restaurantapp.ui.favs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.adapter.FavListAdapter
import hu.zsof.restaurantapp.databinding.FavListFragmentBinding
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.util.Constants
import hu.zsof.restaurantapp.util.extensions.safeNavigate
import hu.zsof.restaurantapp.util.listeners.FavBtnClickListener
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavListFragment : Fragment() {

    private lateinit var binding: FavListFragmentBinding
    private lateinit var listAdapter: FavListAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: FavListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fav_list_fragment, container, false)

        recyclerView = binding.recyclerRestaurantList

        // Get user's favIdsList to compare the full list -> if both contains the place, fill the fav icon
        val userJson = viewModel.getAppPreference<String>(Constants.USER_DATA)
        val user = Gson().fromJson(userJson, User::class.java)

        val userFavIdsList = user.favPlaceIds
        listAdapter = FavListAdapter(
            object : FavBtnClickListener {
                override fun onFavBtnClicked(placeId: Long) {
                    lifecycleScope.launch {
                        val user = viewModel.addOrRemoveFavPlace(placeId)

                        val userJson = Gson().toJson(user)
                        viewModel.setAppPreference(Constants.USER_DATA, userJson)
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
                safeNavigate(FavListFragmentDirections.actionFavListFrToFilterDialogFr())
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.requestPlaceData()
        viewModel.favPlaces.observe(viewLifecycleOwner) {
            listAdapter.restaurantList = it
            binding.recyclerRestaurantList.adapter = listAdapter
        }
    }
}
