package com.example.tourismo.api


import com.example.tourismo.api.response.ApiResponse
import com.example.tourismo.api.response.TiketResponse
import com.example.tourismo.api.response.UploadResponse
import com.example.tourismo.dataclass.TouristDestination
import com.example.tourismo.dataclass.TouristDestinationDetails
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {



//    @FormUrlEncoded()
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("register")
    fun registerUser(
    @Body requestBody: RequestBody


    ): Call<ApiResponse>
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    fun loginUser(
    @Body requestBody: RequestBody


    ): Call<ApiResponse>


    @Multipart
    @POST("predictimage")
    fun uploadPhoto(
        @Header("Authorization")  authorization: String,
        @Part image: MultipartBody.Part
    ): Call<UploadResponse>

    @POST("gettiket")
    fun searchTickets(
        @Header("Authorization")  authorization: String,
        @Body requestBody: RequestBody): Call<TiketResponse>


    @GET("getalltourist")
    fun getAllTourist(
        @Header("Authorization")  authorization: String
    ): Call<List<TouristDestination>>

    @POST("specifictourist")
    fun getSpecificTourist(
        @Header("Authorization")  authorization: String,
        @Body requestBody: JsonObject): Call<TouristDestinationDetails>

//    //Untuk Pencarian User
//    @GET("search/users")
////    @GET("users/{username}")
//    @Headers("Authorization: token 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")
//    fun pullResult(
//        @Query("q") query: String
////        @Path("username") username: String
//    ): Call<UserResponse>
//
//
//    // untuk Mencari detail per user
//    //Trigger click RV lalu onCreate DetailAct
//    @GET("users/{username}")
//    @Headers("Authorization: token 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")
//    fun getUserDetail(
//        @Path("username") username: String
//    ): Call<UserDetailResponse>
//
//
//
//    @GET("users/{username}/followers")
//    @Headers("Authorization: token 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")
//    fun getFollowers(
//        @Path("username") username: String
//    ): Call<ArrayList<User>>
//
//
//    @GET("users/{username}/following")
//    @Headers("Authorization: token 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")
//    fun getFollowing(
//        @Path("username") username: String
//    ): Call<ArrayList<User>>

}
