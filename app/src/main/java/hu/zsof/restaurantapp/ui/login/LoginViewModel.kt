package hu.zsof.restaurantapp.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.model.LoginData
import hu.zsof.restaurantapp.network.model.NetworkResponse
import hu.zsof.restaurantapp.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun login(loginData: LoginData): NetworkResponse {
        return authRepository.loginUser(loginData)
    }
}
