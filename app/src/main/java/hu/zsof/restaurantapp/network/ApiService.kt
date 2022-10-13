package hu.zsof.restaurantapp.network

import androidx.databinding.library.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.response.NetworkResponse
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

interface ApiService {

    @GET("places/")
    suspend fun getAllPlace(): List<PlaceDataRequest>

    @POST("auth/register")
    suspend fun registerUser(@Body loginDataRequest: LoginDataRequest): NetworkResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginDataRequest: LoginDataRequest): NetworkResponse

    @POST("user/newplace")
    suspend fun addNewPlace(@Body placeDataRequest: PlaceDataRequest): NetworkResponse

    @Module
    @InstallIn(SingletonComponent::class)
    object ApiModule {

        @Singleton
        @Provides
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
