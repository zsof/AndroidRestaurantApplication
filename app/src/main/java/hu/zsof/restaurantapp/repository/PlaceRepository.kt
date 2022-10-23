package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.network.response.NetworkResponse
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllPlace(): List<Place> {
        return try {
            apiService.getAllPlace()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    suspend fun addNewPlace(placeDataRequest: PlaceDataRequest): NetworkResponse {
        return try {
            apiService.addNewPlace(placeDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(false)
        }
    }
}
