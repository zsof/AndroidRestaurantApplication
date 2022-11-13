package hu.zsof.restaurantapp.network.response

data class NetworkResponse(
    val isSuccess: Boolean = false,
    val success: String = "",
    val error: String = ""
)
