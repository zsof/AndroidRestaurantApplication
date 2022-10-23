package hu.zsof.restaurantapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.repository.PlaceRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val placeRepository: PlaceRepository) : ViewModel() {

    var places = MutableLiveData<List<Place>>()
    fun requestPlaceData() {
        viewModelScope.launch {
            places.postValue(placeRepository.getAllPlace())
        }
    }
}
