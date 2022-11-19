package hu.zsof.restaurantapp.ui.newplace

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
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
import hu.zsof.restaurantapp.util.Constants.LISTENER
import hu.zsof.restaurantapp.util.extensions.isEmailValid
import hu.zsof.restaurantapp.util.listeners.OnDialogCloseListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewPlaceDialogFragment : DialogFragment() {

    private lateinit var binding: NewPlaceDialogfragmentBinding
    private val viewModel: NewPlaceDialogViewModel by viewModels()

    //private lateinit var closeListener: OnDialogCloseListener

    private lateinit var startForPhotoResult: ActivityResultLauncher<Intent>
    private lateinit var writeExternalPermission: ActivityResultLauncher<String>

    private var currentPhotoPath: String? = ""
    private var priceValue = Price.LOW

    private var latLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            latLng = it.get(LATLNG) as LatLng?
            //closeListener = it.get(LISTENER) as OnDialogCloseListener
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

        /* if (latLng != null) {
             binding.addressEditText.setText(getAddress(latLng!!))
         } else binding.addressEditText.setText("")*/
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_new_place_title))
            .setView(binding.root)
            .setPositiveButton(R.string.save_btn, null)
            .setNegativeButton(R.string.cancel_btn, null)
            .create()

        dialog.setOnShowListener {
            val okButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                checkAllRequiredFieldDone()
            }
        }

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        return dialog
    }



    /*override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }*/

    private fun checkAllRequiredFieldDone() {
        binding.apply {
            val name = placeNameEditText.text.toString()
            val address = addressEditText.text.toString()

            if (name == "" || name == " " || address == "" || address == " ") {
                showDialog(
                    title = getString(R.string.required_field),
                    setView = view,
                    message = (getString(R.string.warning_fill_required_fields)),
                    textPositiveBtn = getString(R.string.ok_btn),
                    onPositiveButton = {}
                )
            } else {
                checkValidFields()
            }
        }
    }

    private fun checkValidFields() {
        binding.apply {
            val email = emailEditText.text.toString()
            val phoneNumber = phoneEditText.text.toString()

            if (email != "" || phoneNumber != "") {
                if (!emailEditText.isEmailValid()) {
                    showDialog(
                        title = getString(R.string.invalid_fields),
                        setView = view,
                        message = getString(R.string.warning_invalid_email_or_phone),
                        textPositiveBtn = getString(R.string.ok_btn),
                        onPositiveButton = {}
                    )
                } else {
                    checkFieldsDone()
                }
            } else {
                checkFieldsDone()
            }
        }
    }

    private fun checkFieldsDone() {
        binding.apply {
            val web = websiteEditText.text.toString()
            val email = emailEditText.text.toString()
            val phoneNumber = phoneEditText.text.toString()

            if (web == "" || phoneNumber == "" || email == "") {
                showDialog(
                    title = getString(R.string.empty_field_title),
                    setView = view,
                    message = getString(R.string.warning_missing_fields),
                    textPositiveBtn = getString(R.string.save_btn),
                    textNegativeBtn = getString(R.string.contuine_btn),
                    onPositiveButton = {
                        savePlace()
                        dismiss()
                        //closeListener.onDialogClosed()
                    }
                )
            } else {
                savePlace()
                dismiss()
                //closeListener.onDialogClosed()
            }
        }
    }

    private fun savePlace() {
        var photoUrl = ""
        if (currentPhotoPath != null && currentPhotoPath != "") {
            photoUrl = currentPhotoPath ?: ""
        }

        binding.apply {
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

    private fun showDialog(
        title: String?,
        setView: View?,
        message: String,
        textPositiveBtn: String,
        textNegativeBtn: String? = null,
        onPositiveButton: () -> Unit
    ) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(setView)
            .setMessage(message)
            .setPositiveButton(textPositiveBtn) { _, _ ->
                onPositiveButton()
            }
            .setNegativeButton(textNegativeBtn) { dialog, _ ->
                dialog.cancel()
            }
            .create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        dialog.show()
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
