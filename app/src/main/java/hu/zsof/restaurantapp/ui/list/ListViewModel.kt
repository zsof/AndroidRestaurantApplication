package hu.zsof.restaurantapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.zsof.restaurantapp.model.PlaceData
import hu.zsof.restaurantapp.repository.DataSyncRepository
import kotlinx.coroutines.launch

class ListViewModel(private val dataSyncRepository: DataSyncRepository) : ViewModel() {

    var places = MutableLiveData<List<PlaceData>>()
    fun requestPlaceData() {
        viewModelScope.launch {
            places.postValue(dataSyncRepository.getPlaces())
        }
    }
}
