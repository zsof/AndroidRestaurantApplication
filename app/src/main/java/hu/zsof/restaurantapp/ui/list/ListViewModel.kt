package hu.zsof.restaurantapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.zsof.restaurantapp.network.model.PlaceDataRequest
import kotlinx.coroutines.launch

class ListViewModel() : ViewModel() {

   /* var places = MutableLiveData<List<PlaceDataRequest>>()
    fun requestPlaceData() {
        viewModelScope.launch {
            places.postValue(dataSyncRepository.getPlaces())
        }
    }*/
}
