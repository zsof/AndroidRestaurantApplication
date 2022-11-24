package hu.zsof.restaurantapp.network.model

data class User(
    val id: Long = 0,
    val name: String? = "",
    val nickName: String? = "",
    val email: String = "",
    val image: String? = null,
    val admin: Boolean = false,
    val favPlaceIds: MutableList<Long> = mutableListOf()
)
