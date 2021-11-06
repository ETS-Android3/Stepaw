package com.butterflies.stepaw.Network

import com.butterflies.stepaw.Network.models.Dogs
import com.butterflies.stepaw.Network.models.Users
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("path")
    fun listDogs(): Call<List<Dogs>>

    @GET("path")
    fun listUsers(): Call<List<Users>>


    companion object{
        const val BASE_URL=""
    }
}