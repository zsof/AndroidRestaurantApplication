package hu.zsof.restaurantapp.ui.newplace

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.NewPlaceDialogfragmentBinding
import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type
import hu.zsof.restaurantapp.network.model.Filter
import hu.zsof.restaurantapp.network.model.PlaceDataRequest

class NewPlaceDialogFragment : DialogFragment() {

    private lateinit var binding: NewPlaceDialogfragmentBinding
    private val viewModel: NewPlaceDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = NewPlaceDialogfragmentBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_new_place_title))
            .setView(binding.root)
            .setPositiveButton(R.string.save_btn) { _, _ ->
                savePlace()
            }
            .setNegativeButton(R.string.cancel_btn, null)
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        return dialog
    }

    private fun savePlace() {
        var priceValue = 0F
        binding.apply {
            /*priceSlider.addOnChangeListener { _, value, _ ->
                priceValue = value
            }*/
            /*viewModel.addNewPlace(
                PlaceDataRequest(
                    name = placeNameEditText.text.toString(),
                    address = addressEditText.text.toString(),
                    type = Type.getByOrdinal(placeCategorySpinner.selectedItemPosition),
                    price = Price.valueOf(priceValue.toString()),
                    image = "",
                    filter = Filter(
                        freeParking = parkingAdd.isChecked,
                        glutenFree = glutenFreeAdd.isChecked,
                        lactoseFree = lactoseFreeAdd.isChecked,
                        vegetarian = vegetarianAdd.isChecked
                    )
                )
            )*/
        }
    }
}