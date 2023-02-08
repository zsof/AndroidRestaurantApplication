package hu.zsof.restaurantapp.repository

import com.google.android.gms.maps.model.LatLng
import hu.zsof.restaurantapp.network.model.Place

object LocalDataStateService {

    private var place: Place? = null
    private var latLng: LatLng? = null

    fun getPlace(): Place {
        if (place == null) {
            throw Exception("Hiba történt, nincs a megadott helyszín")
        } else {
            return place!!
        }
    }

    fun setPlace(place: Place) {
        this.place = place
    }

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
