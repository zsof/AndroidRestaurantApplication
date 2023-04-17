package hu.zsof.restaurantapp.repository

import android.util.Base64
import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.response.LoggedUserResponse
import hu.zsof.restaurantapp.network.response.NetworkResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun registerUser(
        loginDataRequest: LoginDataRequest,
        isAdmin: Boolean,
    ): NetworkResponse {
        return try {
            apiService.registerUser(loginDataRequest, isAdmin)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(false, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun loginUser(loginDataRequest: LoginDataRequest): LoggedUserResponse {
        return try {
            val simpleData = "${loginDataRequest.email}:${loginDataRequest.password}"
            val encodedData =
                android.util.Base64.encodeToString(simpleData.toByteArray(), Base64.NO_WRAP)
            apiService.loginUser("Basic $encodedData")
        } catch (e: Exception) {
            e.printStackTrace()
            LoggedUserResponse(false, e.localizedMessage ?: "Network error")
        }
    }
}
