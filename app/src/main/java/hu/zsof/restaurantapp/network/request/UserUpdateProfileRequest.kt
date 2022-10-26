package hu.zsof.restaurantapp.network.request

data class UserUpdateProfileRequest(
    val name: String? = "",
    val nickName: String? = "",
    val password: String? = "",
    val image: String? = ""
)
