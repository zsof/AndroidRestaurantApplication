package hu.zsof.restaurantapp.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.repository.PlaceRepository
import hu.zsof.restaurantapp.repository.UserRepository
import hu.zsof.restaurantapp.util.extensions.SharedPreference
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreference
) :
    ViewModel() {

    var places = MutableLiveData<List<Place>>()
    fun requestPlaceData() {
        viewModelScope.launch {
            places.postValue(placeRepository.getAllPlace())
        }
    }

    suspend fun addOrRemoveFavPlace(placeId: Long): User? {
        return userRepository.addOrRemoveFavPlace(placeId)
    }

    fun <T> setAppPreference(key: String, value: T) {
        sharedPref.setPreference(key, value)
    }

    fun <T> getAppPreference(key: String): T {
        return sharedPref.getPreference(key)
    }
}
