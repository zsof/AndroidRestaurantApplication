package hu.zsof.restaurantapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import hu.zsof.restaurantapp.network.model.Place

object LocalDataStateService {

    var place: Place = Place()
    private var latLng: LatLng? = null

    var isUserAdmin = MutableLiveData(false)

    fun getLatLng(): LatLng {
        if (latLng == null) {
            throw Exception("Hiba történt, nincs ilyen koordináta")
        } else {
            return latLng!!
        }
    }

    fun setLatLng(latLng: LatLng) {
        this.latLng = latLng
    }
}
