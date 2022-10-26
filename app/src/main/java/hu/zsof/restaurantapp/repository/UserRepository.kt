package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.network.request.UserUpdateProfileRequest
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getUserProfile(): User {
        return try {
            apiService.getUserProfile()
        } catch (e: Exception) {
            e.printStackTrace()
            User()
        }
    }

    suspend fun updateUserProfile(userUpdateProfileRequest: UserUpdateProfileRequest): User { // User vay NetworkResponse??
        return try {
            apiService.updateUserProfile(userUpdateProfileRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            User()
        }
    }
}
