package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.PlaceInReview
import hu.zsof.restaurantapp.network.request.PlaceDataRequest

import javax.inject.Inject

class PlaceOwnerRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addNewPlace(
        placeDataRequest: PlaceDataRequest,
    ): PlaceInReview? {
        return try {
            apiService.addNewPlace(placeDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deletePlace(placeId: Long) {
        try {
            apiService.deletePlace(placeId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
