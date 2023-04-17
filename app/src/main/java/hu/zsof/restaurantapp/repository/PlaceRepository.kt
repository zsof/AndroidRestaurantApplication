package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.request.FilterRequest
import hu.zsof.restaurantapp.network.response.PlaceMapResponse
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

    suspend fun getAllPlaceInMap(): List<PlaceMapResponse> {
        return try {
            apiService.getAllPlaceInMap()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    suspend fun getPlaceById(placeId: Long): Place? {
        return try {
            apiService.getPlaceById(placeId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun filterPlaces(filterItems: FilterRequest): List<Place> {
        return try {
            apiService.filterPlaces(filterItems)
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }
}
