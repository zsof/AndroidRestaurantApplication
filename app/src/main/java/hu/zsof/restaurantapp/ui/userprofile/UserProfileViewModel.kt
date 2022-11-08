package hu.zsof.restaurantapp.ui.userprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.network.request.UserUpdateProfileRequest
import hu.zsof.restaurantapp.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
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
}
