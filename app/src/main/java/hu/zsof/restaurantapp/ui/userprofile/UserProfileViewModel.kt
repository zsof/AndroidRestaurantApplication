package hu.zsof.restaurantapp.ui.userprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.network.request.UserUpdateProfileRequest
import hu.zsof.restaurantapp.repository.UserRepository
import hu.zsof.restaurantapp.util.extensions.SharedPreference
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPref: SharedPreference,
) :
    ViewModel() {

    val userProfile = MutableLiveData<User>()
    fun getUserProfile() {
        viewModelScope.launch {
            userProfile.postValue(userRepository.getUserProfile())
        }
    }

    fun updateUserProfile(userUpdateProfileRequest: UserUpdateProfileRequest) {
        viewModelScope.launch {
            userRepository.updateUserProfile(userUpdateProfileRequest)
            // todo check if userProfile refreshes after update
        }
    }

    fun <T> setAppPreference(key: String, value: T) {
        sharedPref.setPreference(key, value)
    }

    fun <T> getAppPreference(key: String): T {
        return sharedPref.getPreference(key)
    }
}
