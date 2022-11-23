package hu.zsof.restaurantapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.adapter.ListAdapter
import hu.zsof.restaurantapp.databinding.ListFragmentBinding
import hu.zsof.restaurantapp.util.extensions.safeNavigate
import hu.zsof.restaurantapp.util.listeners.FavBtnClickListener
import hu.zsof.restaurantapp.util.listeners.OnDialogCloseListener

@AndroidEntryPoint
class ListFragment : Fragment(), OnDialogCloseListener, FavBtnClickListener {

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
        //todo user-t eltárolni legelején, és onnan elkérni a favlistát- ezt betölteni  ++ todo backenden addfav usert adjon vissza, ne place-t
        val testList = mutableListOf<Long>()
        testList.add(1)
        listAdapter = ListAdapter(this, testList )

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
        }
    }

    private fun subscribeToObservers() {
        viewModel.requestPlaceData()
        viewModel.places.observe(viewLifecycleOwner) {
            listAdapter.restaurantList = it
            binding.recyclerRestaurantList.adapter = listAdapter
        }
    }

    override fun onDialogClosed() {
        viewModel.requestPlaceData()
    }

    override fun onFavBtnClicked(placeId: Long) {
        viewModel.addOrRemoveFavPlace(placeId)
    }
}
