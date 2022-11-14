package hu.zsof.restaurantapp.network.request

import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type
import hu.zsof.restaurantapp.network.model.Filter

data class PlaceDataRequest(
    val name: String = "",
    val address: String = "",
    val web: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val type: Type = Type.RESTAURANT,
    val price: Price = Price.LOW,
    val image: String = "",
    val filter: Filter = Filter(),
    val rate: Float = 2.0f
)
