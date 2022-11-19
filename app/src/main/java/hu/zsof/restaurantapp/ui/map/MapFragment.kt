package hu.zsof.restaurantapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.MapFragmentBinding
import hu.zsof.restaurantapp.util.extensions.showToast

class MapFragment : Fragment() {

    private lateinit var binding: MapFragmentBinding
    private lateinit var map: GoogleMap
    private lateinit var locationPermRequest: ActivityResultLauncher<String>

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        handleFineLocationPermission()
        val budapest = LatLng(47.497913, 19.040236)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(budapest))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11F))

        googleMap.uiSettings.setAllGesturesEnabled(true)
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        /*googleMap.setOnMapLongClickListener {
            val action =
                MapFragmentDirections.actionMapFrToAddPlaceDialogFr(latLng = it)
            findNavController().navigate(action)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MapFragmentBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationPermRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    showToast(getString(R.string.permission_granted))
                    // Log.d(TAG, "Permission granted for location data")
                    enableMyLocation()
                } else {
                    showToast(getString(R.string.permission_denied))
                    // Log.d(TAG, "Permission denied for location data")
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) {
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun handleFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity as AppCompatActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showRationaleDialog(
                    explanation = R.string.permission_explanation_location,
                    onPositiveButton = this::requestFineLocationPermission
                )
            } else {
                requestFineLocationPermission()
            }
        } else {
            enableMyLocation()
        }
    }

    private fun showRationaleDialog(
        title: String = getString(R.string.permission_request),
        explanation: Int,
        onPositiveButton: () -> Unit,
        onNegativeButton: () -> Unit = this::onDestroy
    ) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(explanation)
            .setPositiveButton(getString(R.string.ok_btn)) { dialog, _ ->
                dialog.cancel()
                onPositiveButton()
            }
            .setNegativeButton(getString(R.string.cancel_btn)) { _, _ -> onNegativeButton() }
            .create()
        alertDialog.show()
    }

    private fun requestFineLocationPermission() {
        locationPermRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
