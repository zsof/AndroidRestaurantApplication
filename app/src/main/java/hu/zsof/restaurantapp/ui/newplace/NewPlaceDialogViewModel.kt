package hu.zsof.restaurantapp.ui.newplace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.repository.PlaceRepository
import hu.zsof.restaurantapp.repository.ResourceRepository
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class NewPlaceDialogViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val resourceRepository: ResourceRepository
) : ViewModel() {

    fun addNewPlace(
        placeDataRequest: PlaceDataRequest,
        image: String
    ) {
        try {
            // viewmodel scope megszűnik, amint bezáródik a dialog, ezért kell coroutinescope
            // todo valami szebb megoldás

            CoroutineScope(Job() + Dispatchers.IO).launch {
                val placeResponse = placeRepository.addNewPlace(placeDataRequest)

                if (placeResponse != null) {
                    resourceRepository.addNewImage(
                        filePath = image,
                        type = "place",
                        typeId = placeResponse.id
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
