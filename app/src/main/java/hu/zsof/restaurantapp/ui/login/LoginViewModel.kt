package hu.zsof.restaurantapp.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.response.LoggedUserResponse
import hu.zsof.restaurantapp.network.response.NetworkResponse
import hu.zsof.restaurantapp.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun login(loginDataRequest: LoginDataRequest): LoggedUserResponse {
        return authRepository.loginUser(loginDataRequest)
    }
}
