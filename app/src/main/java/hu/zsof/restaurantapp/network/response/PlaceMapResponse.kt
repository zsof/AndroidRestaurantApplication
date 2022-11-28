package hu.zsof.restaurantapp.network.response

data class PlaceMapResponse(
    val id: Long,
    var name: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
