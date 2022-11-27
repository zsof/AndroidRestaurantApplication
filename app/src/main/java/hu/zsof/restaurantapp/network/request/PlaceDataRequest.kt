package hu.zsof.restaurantapp.network.request

import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type
import hu.zsof.restaurantapp.network.model.CustomFilter

data class PlaceDataRequest(
    val name: String = "",
    val address: String = "",
    val web: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val type: Type = Type.RESTAURANT,
    val price: Price = Price.LOW,
    val image: String = "",
    val customFilter: CustomFilter = CustomFilter(),
    val rate: Float = 2.0f,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
