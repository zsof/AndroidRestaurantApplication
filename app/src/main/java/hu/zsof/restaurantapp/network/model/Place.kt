package hu.zsof.restaurantapp.network.model

import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.enums.Type
import java.io.Serializable

data class Place(
    val id: Long,
    var name: String = "",
    var address: String = "",
    var phoneNumber: String? = "",
    var email: String? = "",
    var web: String? = "",
    var type: Type = Type.RESTAURANT,
    var rate: Float = 2.0f,
    var price: Price = Price.LOW,
    var image: String? = "",
    var filter: Filter = Filter(),
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var usersNumber: Int = 0
) : Serializable
