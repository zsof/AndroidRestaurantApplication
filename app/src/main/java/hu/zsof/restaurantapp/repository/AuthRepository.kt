package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.LoginData
import hu.zsof.restaurantapp.network.model.NetworkResponse
import kotlin.math.log

class AuthRepository(private val apiService: ApiService) {

    suspend fun registerUser(loginData: LoginData): NetworkResponse {
        return try {
            apiService.registerUser(loginData)
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResponse(false, e.localizedMessage ?: "Network error")
        }
    }

    suspend fun loginUser(loginData: LoginData): NetworkResponse {
        return try {
            apiService.loginUser(loginData)
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResponse(false, e.localizedMessage ?: "Network error")
        }
    }
}
