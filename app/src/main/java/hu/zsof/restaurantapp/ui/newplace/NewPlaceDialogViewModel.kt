package hu.zsof.restaurantapp.ui.newplace

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.repository.PlaceOwnerRepository
import hu.zsof.restaurantapp.repository.ResourceRepository
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class NewPlaceDialogViewModel @Inject constructor(
    private val placeOwnerRepository: PlaceOwnerRepository,
    private val resourceRepository: ResourceRepository,
) : ViewModel() {

    fun addNewPlace(
        placeDataRequest: PlaceDataRequest,
        image: String,
    ) {
        try {
            // viewmodel scope megszűnik, amint bezáródik a dialog, ezért kell coroutinescope
            // todo valami szebb megoldás

            CoroutineScope(Job() + Dispatchers.IO).launch {
                val placeResponse = placeOwnerRepository.addNewPlace(placeDataRequest)

                if (placeResponse != null) {
                    resourceRepository.addNewImage(
                        filePath = image,
                        type = "place",
                        itemId = placeResponse.id,
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
