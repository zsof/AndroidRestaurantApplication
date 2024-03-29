package hu.zsof.restaurantapp.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.FilterPlacesDialogfragmentBinding
import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type
import hu.zsof.restaurantapp.network.model.CustomFilter
import hu.zsof.restaurantapp.network.request.FilterRequest
import hu.zsof.restaurantapp.util.Constants

@AndroidEntryPoint
class FilterPlaceDialogFragment : DialogFragment() {

    private lateinit var binding: FilterPlacesDialogfragmentBinding
    private val viewModel: FilterPlaceDialogViewModel by viewModels()

    private var priceValue = Price.LOW
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        binding = FilterPlacesDialogfragmentBinding.inflate(layoutInflater)

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

        dialog.window?.attributes?.windowAnimations = R.style.DialogFragmentAnimation
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun filterPlaces() {
        binding.apply {
            val responseFilter = viewModel.filterPlaces(
                FilterRequest(
                    filter = CustomFilter(
                        glutenFree = glutenFreeAdd.isChecked,
                        lactoseFree = lactoseFreeAdd.isChecked,
                        vegetarian = vegetarianAdd.isChecked,
                        vegan = veganAdd.isChecked,
                        fastFood = fastFoodAdd.isChecked,
                        parkingAvailable = parkingAdd.isChecked,
                        dogFriendly = dogAdd.isChecked,
                        familyPlace = familyPlaceAdd.isChecked,
                        delivery = deliveryAdd.isChecked,
                        creditCard = creditCardAdd.isChecked,
                    ),
                    type = Type.getByOrdinal(placeCategorySpinner.selectedItemPosition),
                ),
            )

            // Returning result to the previous destination
            val filteredPlaces = Gson().toJson(responseFilter)
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                Constants.FILTERED_PLACES,
                filteredPlaces,
            )
        }
    }
}
