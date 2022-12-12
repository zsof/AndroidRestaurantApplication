package hu.zsof.restaurantapp.repository

import hu.zsof.restaurantapp.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ResourceRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun addNewImage(
        filePath: String,
        typeId: Long,
        type: String
    ) {
        return try {
            val file = File(filePath)
            val requestFile = file.asRequestBody("file".toMediaTypeOrNull())
            val multipartFile =
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestFile
                )

            apiService.addNewImage(
                multipartFile,
                type,
                typeId.toString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
