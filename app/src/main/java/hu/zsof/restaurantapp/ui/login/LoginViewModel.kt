package hu.zsof.restaurantapp.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.response.LoggedUserResponse
import hu.zsof.restaurantapp.repository.AuthRepository
import hu.zsof.restaurantapp.util.extensions.SharedPreference
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPref: SharedPreference
) : ViewModel() {

    suspend fun login(loginDataRequest: LoginDataRequest): LoggedUserResponse {
        return authRepository.loginUser(loginDataRequest)
    }

    fun <T> setAppPreference(key: String, value: T) {
        sharedPref.setPreference(key, value)
    }

    fun <T> getAppPreference(key: String): T {
        return sharedPref.getPreference(key)
    }
}
