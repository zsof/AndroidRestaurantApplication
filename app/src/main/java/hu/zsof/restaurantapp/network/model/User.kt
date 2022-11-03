package hu.zsof.restaurantapp.network.model

import hu.zsof.restaurantapp.network.request.PlaceDataRequest

data class User(
    val id: Long = 0,
    val name: String? = "",
    val nickName: String? = "",
    val email: String = "",
    val image: String? = null,
    val admin: Boolean = false,
    val favPlaces: MutableList<PlaceDataRequest> = mutableListOf()
)
