package hu.zsof.restaurantapp.network.request

data class LoginDataRequest(
    val email: String = "",
    val password: String = ""
)
