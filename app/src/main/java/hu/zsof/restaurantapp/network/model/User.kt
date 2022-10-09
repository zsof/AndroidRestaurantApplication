package hu.zsof.restaurantapp.network.model

import hu.zsof.restaurantapp.database.model.PlaceData

data class User(
    val id: Long = 0,
    val name: String = "",
    val nickName: String? = null,
    val email: String = "",
    val image: String? = null,
    val admin: Boolean = false,
    val favPlaces: MutableList<PlaceData> = mutableListOf()
)
