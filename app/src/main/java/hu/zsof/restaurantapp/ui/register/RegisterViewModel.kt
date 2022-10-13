package hu.zsof.restaurantapp.ui.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.response.NetworkResponse
import hu.zsof.restaurantapp.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun register(loginDataRequest: LoginDataRequest): NetworkResponse {
        return authRepository.registerUser(loginDataRequest)
    }
}
