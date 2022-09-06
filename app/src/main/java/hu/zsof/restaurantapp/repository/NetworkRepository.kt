package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.model.PlaceData
import hu.zsof.restaurantapp.network.ApiService

class NetworkRepository(private val apiService: ApiService) {

    suspend fun getAllPlace(): List<PlaceData> {
        return try {
            apiService.getAllPlace()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }
}