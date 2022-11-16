package hu.zsof.restaurantapp.ui.newplace

import android.app.Dialog
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.NewPlaceDialogfragmentBinding
import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type
import hu.zsof.restaurantapp.network.model.Filter
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.util.Constants.LATLNG
import java.util.*

@AndroidEntryPoint
class NewPlaceDialogFragment : DialogFragment() {

    private lateinit var binding: NewPlaceDialogfragmentBinding
    private val viewModel: NewPlaceDialogViewModel by viewModels()
    private var latLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            latLng = it.get(LATLNG) as LatLng?
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = NewPlaceDialogfragmentBinding.inflate(LayoutInflater.from(requireContext()))

       /* if (latLng != null) {
            binding.addressEditText.setText(getAddress(latLng!!))
        } else binding.addressEditText.setText("")*/
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

    // todo layout - elkülöníteni a dolgokat + "filters" legyen catgeroy és place helyett
    private fun savePlace() {
        var priceValue = 0F
        binding.apply {
            priceSlider.addOnChangeListener { _, value, _ ->
                priceValue = value
            }
            viewModel.addNewPlace(
                PlaceDataRequest(
                    name = placeNameEditText.text.toString(),
                    address = addressEditText.text.toString(),
                    web = websiteEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    phoneNumber = phoneEditText.text.toString(),
                    type = Type.getByOrdinal(placeCategorySpinner.selectedItemPosition),
                    price = Price.getByOrdinal(priceValue),
                    image = "",
                    filter = Filter(
                        freeParking = parkingAdd.isChecked,
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
            )
        }
    }

    // todo nem rakja bele a címet, null-t kap latlng-ként
    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        println("efs ${addresses[0].getAddressLine(0)}")
        return addresses[0].getAddressLine(0)
    }

    // todo kép
}
