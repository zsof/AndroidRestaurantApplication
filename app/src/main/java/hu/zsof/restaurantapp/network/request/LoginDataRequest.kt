package hu.zsof.restaurantapp.network.request

data class LoginDataRequest(
    val email: String,
    val password: String,
    val name: String? = null,
    val nickName: String? = null
)
