package hu.zsof.restaurantapp.model

data class RestaurantData(
    val id: Long,
    val name: String,
    val address: String,
    val rate: Float,
    val price: Float,
    val image: Int
)
