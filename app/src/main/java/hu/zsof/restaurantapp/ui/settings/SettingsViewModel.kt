package hu.zsof.restaurantapp.ui.settings

import androidx.lifecycle.ViewModel
import hu.zsof.restaurantapp.repository.AuthRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel()
