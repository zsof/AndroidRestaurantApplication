package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.database.model.PlaceData
import hu.zsof.restaurantapp.network.ApiService

class PlaceRepository(private val apiService: ApiService) {

    suspend fun getAllPlace(): List<PlaceData> {
        return try {
            apiService.getAllPlace()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }
    suspend fun getPlaces(): List<PlaceData> {
        try {
            return placeRepository.getAllPlace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }
}