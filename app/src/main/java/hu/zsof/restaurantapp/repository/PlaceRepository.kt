package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.network.request.FilterRequest
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
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

    suspend fun addNewPlace(
        placeDataRequest: PlaceDataRequest
    ): Place? {
        return try {
            apiService.addNewPlace(placeDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addOrRemoveFavPlace(placeId: Long): User? {
        return try {
            apiService.addPlaceToFav(placeId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getFavPlacesByUser(): List<Place> {
        return try {
            apiService.getFavPlaces()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
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
