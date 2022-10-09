package hu.zsof.restaurantapp.network

import androidx.databinding.library.BuildConfig
import hu.zsof.restaurantapp.network.model.LoginData
import hu.zsof.restaurantapp.network.model.NetworkResponse
import hu.zsof.restaurantapp.network.model.PlaceData
import hu.zsof.restaurantapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("places/")
    suspend fun getAllPlace(): List<PlaceData>

    @POST("auth/register")
    suspend fun registerUser(@Body loginData: LoginData): NetworkResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginData: LoginData): NetworkResponse

    companion object {
        operator fun invoke(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
            }

            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
