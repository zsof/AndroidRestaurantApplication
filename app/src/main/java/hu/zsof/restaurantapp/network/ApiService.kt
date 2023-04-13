package hu.zsof.restaurantapp.network

import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.network.model.PlaceInReview
import hu.zsof.restaurantapp.network.model.User
import hu.zsof.restaurantapp.network.request.FilterRequest
import hu.zsof.restaurantapp.network.request.LoginDataRequest
import hu.zsof.restaurantapp.network.request.PlaceDataRequest
import hu.zsof.restaurantapp.network.request.UserUpdateProfileRequest
import hu.zsof.restaurantapp.network.response.LoggedUserResponse
import hu.zsof.restaurantapp.network.response.NetworkResponse
import hu.zsof.restaurantapp.network.response.PlaceMapResponse

import okhttp3.*
import retrofit2.http.*

interface ApiService {

    /**
     * Places (role: user, owner, admin)
     */
    @GET("places")
    suspend fun getAllPlace(): List<Place>

    @GET("places/map")
    suspend fun getAllPlaceInMap(): List<PlaceMapResponse>

    @GET("places/{placeId}")
    suspend fun getPlaceById(@Path("placeId") placeId: Long): Place

    @POST("places/filter")
    suspend fun filterPlaces(@Body filter: FilterRequest): List<Place>

    /**
     * Place by Owner (role: owner, admin
     */

    @POST("places-owner/new-place")
    suspend fun addNewPlace(
        @Body placeDataRequest: PlaceDataRequest,
    ): PlaceInReview

    @DELETE("places-owner/places/{id}")
    suspend fun deletePlace(@Path("id") placeId: Long)

    /**
     * Place in-review (role: admin)
     */

    @GET("places-review")
    suspend fun getAllPlaceFromInReview(): List<PlaceInReview>

    @GET("places-review/{id}")
    suspend fun getPlaceByIdFromInReview(@Path("id") placeId: Long): PlaceInReview

    @POST("places-review/accept/{id}")
    suspend fun acceptPlaceFromInReview(@Path("id") placeId: Long): Place

    @POST("places-review/report/{id}")
    suspend fun reportProblemPlaceInReview(
        @Path("id") placeId: Long,
        @Body problemMessage: String,
    ): PlaceInReview

    @DELETE("places-review/places/{id}")
    suspend fun deletePlaceFromInReview(@Path("id") placeId: Long)

    /**
     * Image
     */
    @Multipart
    @POST("images")
    suspend fun addNewImage(
        @Part file: MultipartBody.Part,
        @Part("type") type: String,
        @Part("itemId") typeId: String,
    )

    /**
     * Auth
     */
    @POST("auth/register")
    suspend fun registerUser(@Body loginDataRequest: LoginDataRequest, @Header("isAdmin") isAdmin: Boolean): NetworkResponse

    @POST("auth/login")
    suspend fun loginUser(@Header("Authorization") encodedBasic: String): LoggedUserResponse

    /**
     * Admin
     */
    @GET("admin/users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): User

    @GET("admin/users")
    suspend fun getAllUser(): List<User>

    @DELETE("admin/users/{id}")
    suspend fun deleteUserById(@Path("id") userId: Long)

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
}
