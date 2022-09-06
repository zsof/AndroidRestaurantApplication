package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.model.PlaceData

class DataSyncRepository(private val networkRepository: NetworkRepository) {

    suspend fun getPlaces(): List<PlaceData> {
        try {
            return networkRepository.getAllPlace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }
}
