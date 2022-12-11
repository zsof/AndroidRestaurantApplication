package hu.zsof.restaurantapp.ui.newplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.repository.PlaceRepository
import hu.zsof.restaurantapp.repository.ResourceRepository
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class NewPlaceDialogViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val resourceRepository: ResourceRepository
) : ViewModel() {

    fun addNewPlace(
        placeDataRequest: PlaceDataRequest,
        image: String
    ) {
        try {
            // viewmodel scope megszűnik, amint bezáródik a dialog, ezért kell coroutinescope
            // todo valami szebb megoldás
            val scope = CoroutineScope(Job() + Dispatchers.IO)
            scope.launch {
                val placeResponse = placeRepository.addNewPlace(placeDataRequest)

                if (placeResponse != null) {
                    resourceRepository.addNewImage(
                        filePath = image,
                        type = "place",
                        typeId = placeResponse.id
                    )
                    println("vm image után")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addNewImage(image: String, typeId: Long) {
        try {
            viewModelScope.launch {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

/*    fun addNewPlace(
        filePath: String,
        placeDataRequest: PlaceDataRequest
        *//* name: String,
         address: String,
         web: String,
         email: String,
         phoneNumber: String,
         type: Type,
         price: Price,
         filter: CustomFilter,
         rate: Float = 0f,
         latitude: Double,
         longitude: Double,
         openDetails: OpenDetails*//*
    ) {
        viewModelScope.launch {
            placeRepository.addNewPlace(
                filePath,
                placeDataRequest
                *//* name,
                 address,
                 web,
                 email,
                 phoneNumber,
                 type,
                 price,
                 filter,
                 rate,
                 latitude,
                 longitude,
                 openDetails*//*
            )
        }
    }*/
}
