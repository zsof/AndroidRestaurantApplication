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
import hu.zsof.restaurantapp.network.model.CustomFilter
import hu.zsof.restaurantapp.network.model.OpenDetails
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.repository.LocalDataStateService
import hu.zsof.restaurantapp.util.extensions.isEmailValid
import hu.zsof.restaurantapp.util.extensions.safeNavigate
import hu.zsof.restaurantapp.util.extensions.showToast
import hu.zsof.restaurantapp.util.utils.OpenHoursUtil
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

    private var sameOpenHoursChecked = false

    private lateinit var latLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            latLng = LocalDataStateService.getLatLng()
        } catch (e: Exception) {
            showToast(e.message)
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
                        "RestaurantApp-images",
                    )

                    binding.addPhotoBtn.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            bitmap,
                            binding.addPhotoBtn.width,
                            binding.addPhotoBtn.height,
                            false,
                        ),
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
                            photoFile,
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
        binding = NewPlaceDialogfragmentBinding.inflate(layoutInflater)

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

        binding.addressEditText.setText(getAddress(latLng))

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
        dialog.window?.attributes?.windowAnimations = R.style.DialogFragmentAnimation

        return dialog
    }

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
                    onPositiveButton = {},
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
                        onPositiveButton = {},
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
                        safeNavigate(NewPlaceDialogFragmentDirections.actionNewPlaceDialogFrToListFr())
                    },
                )
            } else {
                savePlace()
                dismiss()
                safeNavigate(NewPlaceDialogFragmentDirections.actionNewPlaceDialogFrToListFr())
            }
        }
    }

    private fun savePlace() {
        var photoUrl = ""
        if (currentPhotoPath != null && currentPhotoPath != "") {
            photoUrl = currentPhotoPath as String
        }

        println("fr image $photoUrl")

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
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    openDetails = OpenDetails(
                        basicOpen.text.toString(),
                        basicClose.text.toString(),
                        mondayOpen.text.toString(),
                        mondayClose.text.toString(),
                        tuesdayOpen.text.toString(),
                        tuesdayClose.text.toString(),
                        wednesdayOpen.text.toString(),
                        wednesdayClose.text.toString(),
                        thursdayOpen.text.toString(),
                        thursdayClose.text.toString(),
                        fridayOpen.text.toString(),
                        fridayClose.text.toString(),
                        saturdayOpen.text.toString(),
                        saturdayClose.text.toString(),
                        sundayOpen.text.toString(),
                        sundayClose.text.toString(),
                        mondayCheckbox.isChecked,
                        tuesdayCheckbox.isChecked,
                        wednesdayCheckbox.isChecked,
                        thursdayCheckbox.isChecked,
                        fridayCheckbox.isChecked,
                        saturdayCheckbox.isChecked,
                        sundayCheckbox.isChecked,
                    ),
                ),
                image = photoUrl,
            )
        }
    }

    private fun showDialog(
        title: String?,
        setView: View?,
        message: String,
        textPositiveBtn: String,
        textNegativeBtn: String? = null,
        onPositiveButton: () -> Unit,
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
        binding.apply {
            addPhotoBtn.setOnClickListener {
                writeExternalPermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            expandIcon.setOnClickListener {
                if (cardGroup.visibility == View.VISIBLE) {
                    expandIcon.setImageResource(R.drawable.ic_expand)
                    cardGroup.visibility = View.GONE
                } else {
                    expandIcon.setImageResource(R.drawable.ic_collapse)
                    cardGroup.visibility = View.VISIBLE
                }
            }

            mondayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    mondayOpen,
                    mondayClose,
                    sameOpenHoursChecked,
                )
            }
            tuesdayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    tuesdayOpen,
                    tuesdayClose,
                    sameOpenHoursChecked,
                )
            }
            wednesdayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    wednesdayOpen,
                    wednesdayClose,
                    sameOpenHoursChecked,
                )
            }
            thursdayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    thursdayOpen,
                    thursdayClose,
                    sameOpenHoursChecked,
                )
            }
            fridayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    fridayOpen,
                    fridayClose,
                    sameOpenHoursChecked,
                )
            }
            saturdayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    saturdayOpen,
                    saturdayClose,
                    sameOpenHoursChecked,
                )
            }
            sundayCheckbox.setOnCheckedChangeListener { _, isChecked ->
                OpenHoursUtil.dayOpenOrClosed(
                    requireContext(),
                    isChecked,
                    sundayOpen,
                    sundayClose,
                    sameOpenHoursChecked,
                )
            }

            sameOpeningCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    sameOpenHoursChecked = true
                    basicOpeningGroup.visibility = View.VISIBLE
                    OpenHoursUtil.disableText(
                        mondayOpen,
                        mondayClose,
                        requireContext(),
                        mondayCheckbox,
                    )
                    OpenHoursUtil.disableText(
                        tuesdayOpen,
                        tuesdayClose,
                        requireContext(),
                        tuesdayCheckbox,
                    )
                    OpenHoursUtil.disableText(
                        wednesdayOpen,
                        wednesdayClose,
                        requireContext(),
                        wednesdayCheckbox,
                    )
                    OpenHoursUtil.disableText(
                        thursdayOpen,
                        thursdayClose,
                        requireContext(),
                        thursdayCheckbox,
                    )
                    OpenHoursUtil.disableText(
                        fridayOpen,
                        fridayClose,
                        requireContext(),
                        fridayCheckbox,
                    )
                    OpenHoursUtil.disableText(
                        saturdayOpen,
                        saturdayClose,
                        requireContext(),
                        saturdayCheckbox,
                    )
                    OpenHoursUtil.disableText(
                        sundayOpen,
                        sundayClose,
                        requireContext(),
                        sundayCheckbox,
                    )
                } else {
                    sameOpenHoursChecked = false
                    basicOpeningGroup.visibility = View.GONE
                    OpenHoursUtil.enableText(mondayOpen, mondayClose)
                    OpenHoursUtil.enableText(tuesdayOpen, tuesdayClose)
                    OpenHoursUtil.enableText(wednesdayOpen, wednesdayClose)
                    OpenHoursUtil.enableText(thursdayOpen, thursdayClose)
                    OpenHoursUtil.enableText(fridayOpen, fridayClose)
                    OpenHoursUtil.enableText(saturdayOpen, saturdayClose)
                    OpenHoursUtil.enableText(sundayOpen, sundayClose)
                }
            }
            basicOpen.setOnClickListener {
                OpenHoursUtil.timePickerBasic(
                    childFragmentManager,
                    basicOpen,
                    mondayOpen,
                    tuesdayOpen,
                    wednesdayOpen,
                    thursdayOpen,
                    fridayOpen,
                    saturdayOpen,
                    sundayOpen,
                )
            }

            basicClose.setOnClickListener {
                OpenHoursUtil.timePickerBasic(
                    childFragmentManager,
                    basicClose,
                    mondayClose,
                    tuesdayClose,
                    wednesdayClose,
                    thursdayClose,
                    fridayClose,
                    saturdayClose,
                    sundayClose,
                )
            }

            mondayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(mondayOpen, childFragmentManager)
            }
            mondayClose.setOnClickListener {
                OpenHoursUtil.timePicker(mondayClose, childFragmentManager)
            }
            tuesdayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(tuesdayOpen, childFragmentManager)
            }
            tuesdayClose.setOnClickListener {
                OpenHoursUtil.timePicker(tuesdayClose, childFragmentManager)
            }
            wednesdayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(wednesdayOpen, childFragmentManager)
            }
            wednesdayClose.setOnClickListener {
                OpenHoursUtil.timePicker(wednesdayClose, childFragmentManager)
            }
            thursdayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(thursdayOpen, childFragmentManager)
            }
            thursdayClose.setOnClickListener {
                OpenHoursUtil.timePicker(thursdayClose, childFragmentManager)
            }
            fridayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(fridayOpen, childFragmentManager)
            }
            fridayClose.setOnClickListener {
                OpenHoursUtil.timePicker(fridayClose, childFragmentManager)
            }
            saturdayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(saturdayOpen, childFragmentManager)
            }
            saturdayClose.setOnClickListener {
                OpenHoursUtil.timePicker(saturdayClose, childFragmentManager)
            }
            sundayOpen.setOnClickListener {
                OpenHoursUtil.timePicker(sundayOpen, childFragmentManager)
            }
            sundayClose.setOnClickListener {
                OpenHoursUtil.timePicker(sundayClose, childFragmentManager)
            }
        }
    }

    private fun getAddress(latLng: LatLng): String? {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        return addresses?.get(0)?.getAddressLine(0)
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
            idsDir, /* directory */
        )
        val path = image.absolutePath
        println("abs path: $path")
        return image
    }
}
