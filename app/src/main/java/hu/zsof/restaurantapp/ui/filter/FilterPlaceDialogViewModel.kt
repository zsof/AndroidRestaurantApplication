package hu.zsof.restaurantapp.ui.filter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.repository.PlaceRepository
import javax.inject.Inject

@HiltViewModel
class FilterPlaceDialogViewModel @Inject constructor(private val placeRepository: PlaceRepository) :
    ViewModel()
