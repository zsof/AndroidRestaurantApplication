package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.response.NetworkResponse
import hu.zsof.restaurantapp.network.request.PlaceDataRequest

class PlaceRepository(private val apiService: ApiService) {

   /* suspend fun getAllPlace(): List<PlaceDataRequest> {
        return try {
            apiService.getAllPlace()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }*/

    suspend fun addNewPlace(placeDataRequest: PlaceDataRequest): NetworkResponse {
        return try {
            apiService.addNewPlace(placeDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(false)
        }
    }
}