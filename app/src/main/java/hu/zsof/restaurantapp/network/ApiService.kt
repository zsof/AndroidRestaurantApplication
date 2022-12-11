package hu.zsof.restaurantapp.network

import androidx.databinding.library.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.network.request.FilterRequest
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.network.request.UserUpdateProfileRequest
import hu.zsof.restaurantapp.network.response.LoggedUserResponse
import hu.zsof.restaurantapp.network.response.NetworkResponse
import hu.zsof.restaurantapp.network.response.PlaceMapResponse
import hu.zsof.restaurantapp.util.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit
import java.util.prefs.Preferences
import javax.inject.Singleton

interface ApiService {

    /**
     * Place
     */
    @GET("places")
    suspend fun getAllPlace(): List<Place>

    @GET("places/map")
    suspend fun getAllPlaceInMap(): List<PlaceMapResponse>

    @POST("places/new-place")
    suspend fun addNewPlace(
        @Body placeDataRequest: PlaceDataRequest
    ): Place

    /*  @Multipart
      @JvmSuppressWildcards
      @POST("places/new-place")
      suspend fun addNewPlace(
          @Part file: MultipartBody.Part?,
          @Part("place") newPlaceRequest: RequestBody
        *//*  @Part file: MultipartBody.Part?,
        @PartMap mapString: Map<String, RequestBody>,
        @PartMap mapDouble: Map<String, Double>,
        @Part("rate") rate: Float,
        @Part("type") type: Type,
        @Part("price") price: Price,
        @Part("filter") filter: CustomFilter,
        @Part("openDetails") openDetails: OpenDetails*//*
    ): Place*/

    @POST("places/filter")
    suspend fun filterPlaces(@Body filter: FilterRequest): List<Place>

    /**
     * Auth
     */
    @POST("auth/register")
    suspend fun registerUser(@Body loginDataRequest: LoginDataRequest): NetworkResponse

    @POST("auth/login")
    suspend fun loginUser(@Body loginDataRequest: LoginDataRequest): LoggedUserResponse

    /**
     * User
     */
    @GET("users/profile")
    suspend fun getUserProfile(): User

    @PUT("users/profile")
    suspend fun updateUserProfile(@Body userUpdateProfileRequest: UserUpdateProfileRequest): User

    @POST("users/fav-places/{placeId}")
    suspend fun addPlaceToFav(@Path("placeId") placeId: Long): User

    @GET("users/fav-places")
    suspend fun getFavPlaces(): List<Place>

    // todo cookie megmaradjon vmennyi ideig - ne kelljen újra bejelentkezni. De ha lejárt, akkor kérjen új bejelentkezést
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
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addInterceptor(ReceivedCookiesInterceptor())
                    .addInterceptor(AddCookiesInterceptor())
                    .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }

        class ReceivedCookiesInterceptor : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val originalResponse: Response = chain.proceed(chain.request())
                if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
                    val cookies = originalResponse.headers("Set-Cookie")

                    Preferences.userRoot().put("cookie", cookies[0])
                    println(cookies[0])
                }
                return originalResponse
            }
        }

        class AddCookiesInterceptor : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val builder: Request.Builder = chain.request().newBuilder()
                val cookie = Preferences.userRoot().get("cookie", "")
                if (cookie.isNotEmpty()) {
                    println("Cookie ->$cookie")
                    builder.addHeader("Cookie", cookie)
                    // Timber.tag("OkHttp").d("Adding Header: %s", cookie)
                } else println("ERROR: NO COOKIE ADDED")
                // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                return chain.proceed(builder.build())
            }
        }
    }
}
