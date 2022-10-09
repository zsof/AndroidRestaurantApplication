package hu.zsof.restaurantapp.network.model

data class PlaceData(
    val id: Long,
    val name: String,
    val address: String,
    val rate: Float,
    val price: Float,
    val image: Int
)
