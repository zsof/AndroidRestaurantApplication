package hu.zsof.restaurantapp.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.FilterPlacesDialogfragmentBinding
import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.model.CustomFilter
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.util.Constants
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterPlaceDialogFragment : DialogFragment() {

    private lateinit var binding: FilterPlacesDialogfragmentBinding
    private val viewModel: FilterPlaceDialogViewModel by viewModels()

    private var priceValue = Price.LOW

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = FilterPlacesDialogfragmentBinding.inflate(LayoutInflater.from(requireContext()))

        binding.priceSlider.addOnChangeListener { _, value, _ ->
            priceValue = when (value) {
                0f -> {
                    Price.LOW
                }
                50f -> {
                    Price.MIDDLE
                }
                else -> {
                    Price.HIGH
                }
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.filter_places_dialog))
            .setView(binding.root)
            .setPositiveButton(R.string.filter_btn) { _, _ ->
                filterPlaces()
                dismiss()
            }
            .setNegativeButton(R.string.cancel_btn, null)
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        return dialog
    }

    private fun filterPlaces() {
        //todo minden lista üres tömböt ad vissza
        var responsePlaces: List<Place> = mutableListOf()
        binding.apply {
            lifecycleScope.launch {
                responsePlaces = viewModel.filterPlaces(
                    CustomFilter(
                        glutenFree = glutenFreeAdd.isChecked,
                        lactoseFree = lactoseFreeAdd.isChecked,
                        vegetarian = vegetarianAdd.isChecked,
                        vegan = veganAdd.isChecked,
                        fastFood = fastFoodAdd.isChecked,
                        parkingAvailable = parkingAdd.isChecked,
                        dogFriendly = dogAdd.isChecked,
                        familyPlace = familyPlaceAdd.isChecked,
                        delivery = deliveryAdd.isChecked,
                        creditCard = creditCardAdd.isChecked
                    )
                )
            }
        }
        println("resp filter $responsePlaces")
        val filteredPlaces = Gson().toJson(responsePlaces)
        val navController = findNavController()

        println("filtered $filteredPlaces")
        // Returning result to the previous destination
        navController.previousBackStackEntry?.savedStateHandle?.set(
            Constants.FILTERED_PLACES,
            filteredPlaces
        )
    }
}
