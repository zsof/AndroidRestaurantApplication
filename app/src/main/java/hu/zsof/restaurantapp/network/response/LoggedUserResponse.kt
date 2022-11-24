package hu.zsof.restaurantapp.network.response

import hu.zsof.restaurantapp.network.model.User

data class LoggedUserResponse(
    val isSuccess: Boolean = false,
    val successMessage: String = "",
    val error: String = "",
    val user: User? = null
)
