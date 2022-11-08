package hu.zsof.restaurantapp.network.request

data class UserUpdateProfileRequest(
    val name: String? = null,
    val nickName: String? = null,
    val password: String? = null,
    val image: String? = null,
    val email: String? = null
)
