package hu.zsof.restaurantapp.ui.newplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.repository.PlaceRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPlaceDialogViewModel @Inject constructor(private val placeRepository: PlaceRepository) :
    ViewModel() {

    fun addNewPlace(
        placeDataRequest: PlaceDataRequest
    ) {
        viewModelScope.launch {
            placeRepository.addNewPlace(
                placeDataRequest
            )
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
