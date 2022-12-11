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
            println("rep eleje")
            val mapString = mutableMapOf<String, String>()

            val file = File(filePath)
            val requestFile = file.asRequestBody("file".toMediaTypeOrNull())
            // val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val multipartFile =
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestFile
                )

            mapString["type"] = type
            mapString["typeId"] = typeId.toString()

            // todo kép mentés nem sikeres mert az id-t a backend így kapja meg ""12"", nem is lehet átalakítani ott, mert bele se fut az ágba, paraméterben már rosszul kapja meg és leáll
            println("rep string $typeId  ${typeId.toString()}   $mapString  ")
            apiService.addNewImage(
                multipartFile,
                mapString
            )

            println("rep vége")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
