package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.response.NetworkResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun registerUser(loginDataRequest: LoginDataRequest): NetworkResponse {
        return try {
            apiService.registerUser(loginDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(false, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun loginUser(loginDataRequest: LoginDataRequest): NetworkResponse {
        return try {
            apiService.loginUser(loginDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(false, e.localizedMessage ?: "Network error")
        }
    }
}
