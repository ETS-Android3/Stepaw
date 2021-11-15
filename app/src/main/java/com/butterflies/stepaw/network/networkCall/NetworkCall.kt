package com.butterflies.stepaw.network.networkCall

import android.util.Log
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.RetrofitObservable
import com.butterflies.stepaw.network.models.PetGetModel
import com.butterflies.stepaw.network.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header

class NetworkCall {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val service: ApiService = retrofit.create(ApiService::class.java)


    fun getAllPets(token:String){
        val pets=service.getAllPets(token=" Bearer $token")
        pets.enqueue(object : Callback<List<PetGetModel>> {
            override fun onResponse(
                call: Call<List<PetGetModel>>,
                response: Response<List<PetGetModel>>
            ) {
                TODO("response.body() for data")
            }

            override fun onFailure(call: Call<List<PetGetModel>>, t: Throwable) {
                TODO("throw error")
            }

        })
    }


    fun getPetById(token:String,id:String){
        val pet=service.getPetById(token=" Bearer $token",id)
        pet.enqueue(object : Callback<PetGetModel> {
            override fun onResponse(call: Call<PetGetModel>, response: Response<PetGetModel>) {
                TODO("response.body()")
            }

            override fun onFailure(call: Call<PetGetModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}