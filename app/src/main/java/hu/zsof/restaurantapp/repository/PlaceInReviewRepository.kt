package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.model.PlaceInReview
import javax.inject.Inject

class PlaceInReviewRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllPlaceFromInReview(): List<PlaceInReview> {
        return try {
            apiService.getAllPlaceFromInReview()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    suspend fun getPlaceByIdFromInReview(placeId: Long): PlaceInReview? {
        return try {
            apiService.getPlaceByIdFromInReview(placeId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun acceptPlaceFromInReview(placeId: Long): Place? {
        return try {
            apiService.acceptPlaceFromInReview(placeId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun reportProblemPlaceInReview(placeId: Long, problemMessage: String): PlaceInReview? {
        return try {
            apiService.reportProblemPlaceInReview(placeId, problemMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deletePlaceFromInReview(placeId: Long) {
        try {
            apiService.deletePlaceFromInReview(placeId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
