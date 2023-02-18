package hu.zsof.restaurantapp.repository

import com.google.android.gms.maps.model.LatLng
import hu.zsof.restaurantapp.network.model.Place

object LocalDataStateService {

    var place: Place = Place()
    private var latLng: LatLng? = null

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
