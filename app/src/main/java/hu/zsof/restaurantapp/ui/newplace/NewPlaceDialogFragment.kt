package hu.zsof.restaurantapp.ui.newplace

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewPlaceDialogFragment : DialogFragment() {

    private lateinit var binding: NewPlaceDialogfragmentBinding
    private val viewModel: NewPlaceDialogViewModel by viewModels()

    private lateinit var startForPhotoResult: ActivityResultLauncher<Intent>
    private lateinit var writeExternalPermission: ActivityResultLauncher<String>

    private var currentPhotoPath: String? = ""
    private var priceValue = Price.LOW

    private var latLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            latLng = it.get(LATLNG) as LatLng?
        }

        startForPhotoResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    var bitmap: Bitmap? = null
                    if (currentPhotoPath != null) {
                        val uri = Uri.fromFile(File(currentPhotoPath!!))
                        bitmap =
                            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    }

                    if (bitmap == null) {
                        return@registerForActivityResult
                    }

                    MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        currentPhotoPath,
                        "RestaurantApp_${System.currentTimeMillis()}",
                        "RestaurantApp-images"
                    )

                    binding.addPhotoBtn.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            bitmap,
                            binding.addPhotoBtn.width,
                            binding.addPhotoBtn.height,
                            false
                        )
                    )
                }
            }

        writeExternalPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionAccepted ->
                if (isPermissionAccepted) {
                    val imageCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                    if (photoFile != null) {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "hu.zsof.restaurantapp.fileProvider",
                            photoFile
                        )

                        currentPhotoPath = photoURI.path
                        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startForPhotoResult.launch(Intent(imageCaptureIntent))
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission is declined", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = NewPlaceDialogfragmentBinding.inflate(LayoutInflater.from(requireContext()))

        setupBindings()

        binding.priceSlider.addOnChangeListener { _, value, _ ->
            when (value) {
                0f -> {
                    priceValue = Price.LOW
                }
                50f -> {
                    priceValue = Price.MIDDLE
                }
                else -> {
                    priceValue = Price.HIGH
                }
            }
        }

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
        binding.apply {
            var photoUrl = ""
            if (currentPhotoPath != null && currentPhotoPath != "") {
                photoUrl = currentPhotoPath ?: ""
            }

            viewModel.addNewPlace(
                PlaceDataRequest(
                    name = placeNameEditText.text.toString(),
                    address = addressEditText.text.toString(),
                    web = websiteEditText.text.toString(),
                    email = emailEditText.text.toString(),
                    phoneNumber = phoneEditText.text.toString(),
                    type = Type.getByOrdinal(placeCategorySpinner.selectedItemPosition),
                    price = priceValue,
                    image = photoUrl,
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

    private fun setupBindings() {
        binding.addPhotoBtn.setOnClickListener {
            writeExternalPermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    // todo nem rakja bele a címet, null-t kap latlng-ként
    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        println("efs ${addresses[0].getAddressLine(0)}")
        return addresses[0].getAddressLine(0)
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        var idsDir: File? = null
        storageDir?.let {
            idsDir = File(storageDir.absolutePath + "/RestaurantApp")

            if (!idsDir!!.exists()) {
                val success = idsDir!!.mkdir()
                if (success) {
                    idsDir = File(storageDir.absolutePath + "/RestaurantApp")
                }
            }
        }

        val image = File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            idsDir /* directory */
        )
        val path = image.absolutePath
        println("abs path: $path")
        return image
    }
}
