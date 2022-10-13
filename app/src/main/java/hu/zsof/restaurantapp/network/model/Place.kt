package hu.zsof.restaurantapp.network.model

import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type

data class Place(
    val id: Long,
    val name: String = "",
    val address: String = "",
    val type: Type = Type.RESTAURANT,
    val rate: Float = 2.0f,
    val price: Price = Price.LOW,
    val image: String = "",
    val filter: Filter = Filter()
)
