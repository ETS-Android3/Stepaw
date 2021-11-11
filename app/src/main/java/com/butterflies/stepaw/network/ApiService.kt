package com.butterflies.stepaw.network

import com.butterflies.stepaw.network.models.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

//    Create new user
    @Headers("Content-Type: application/json")
    @POST("/user/users")
    fun createUser(@Body userModel: UserModel): Call<UserModel>

    @GET("/user/users")
    fun getAllUsers():Call<List<UserModel>>

    companion object {
        const val BASE_URL = "https://stepaw.wmdd4950.com"
    }
}