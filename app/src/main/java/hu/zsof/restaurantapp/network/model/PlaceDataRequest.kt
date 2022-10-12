package hu.zsof.restaurantapp.network.model

import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type

data class PlaceDataRequest(
    val name: String = "",
    val address: String = "",
    val type: Type = Type.RESTAURANT,
    val price: Price = Price.LOW,
    val image: String = "",
    val filter: Filter = Filter()
)
