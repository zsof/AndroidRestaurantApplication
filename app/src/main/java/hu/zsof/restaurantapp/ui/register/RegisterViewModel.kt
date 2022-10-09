package hu.zsof.restaurantapp.ui.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.model.LoginData
import hu.zsof.restaurantapp.network.model.NetworkResponse
import hu.zsof.restaurantapp.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun register(loginData: LoginData): NetworkResponse {
        return authRepository.registerUser(loginData)
    }
}
