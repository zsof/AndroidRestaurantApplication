package hu.zsof.restaurantapp.ui.newplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.repository.PlaceRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewPlaceDialogViewModel @Inject constructor(private val placeRepository: PlaceRepository) :
    ViewModel() {

    fun addNewPlace(placeDataRequest: PlaceDataRequest) {
        viewModelScope.launch {
            placeRepository.addNewPlace(placeDataRequest)
        }
    }
}
