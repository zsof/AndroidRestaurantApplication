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

    // todo nem töldtődik fel, ha nincs kép akkor meg backend not supported ad vissza a multipart/from-data-ra
    /*   suspend fun addNewPlace(
           filePath: String,
           placeDataRequest: PlaceDataRequest
           *//*name: String,
        address: String,
        web: String,
        email: String,
        phoneNumber: String,
        type: Type,
        price: Price,
        filter: CustomFilter,
        rate: Float,
        latitude: Double,
        longitude: Double,
        openDetails: OpenDetails*//*
    ): Place? {
        return try {
            *//*  val mapString = mutableMapOf<String, RequestBody>()
              val mapDouble = mutableMapOf<String, Double>()*//*

            val file = File(filePath)
            // val requestFile = file.asRequestBody("file".toMediaTypeOrNull())
            val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val multipartFile = if (filePath.isEmpty()) null else MultipartBody.Part.createFormData("image", file.name, requestFile)

            val placeDataString = Gson().toJson(placeDataRequest)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("place", placeDataString)
                .build()
*//*
            mapString["name"] = name.toRequestBody()
            mapString["address"] = address.toRequestBody()
            mapString["web"] = web.toRequestBody()
            mapString["email"] = email.toRequestBody()
            mapString["phoneNumber"] = phoneNumber.toRequestBody()

            mapDouble["latitude"] = latitude
            mapDouble["longitude"] = longitude*//*

            apiService.addNewPlace(multipartFile, requestBody*//* mapString, mapDouble, rate, type, price, filter, openDetails*//*)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }*/

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
