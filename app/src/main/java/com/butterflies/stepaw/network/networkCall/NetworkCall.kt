package com.butterflies.stepaw.network.networkCall

import android.util.Log
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.RetrofitObservable
import com.butterflies.stepaw.network.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Retrofit

class NetworkCall {
    val retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val service=retrofit.create(ApiService::class.java)
    fun getAllUsersCall(){
        val users=service.getAllUsers()
        users.enqueue(object : Callback<List<UserModel>> {
            override fun onResponse(
                call: Call<List<UserModel>>,
                response: Response<List<UserModel>>
            ) {
                RetrofitObservable().getInstance()?.notifyObserverWithResponse(response)

            }

            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {

            }

        })
    }

    fun createNewUserCall(UserID: String,
        UserName: String,
        FirstName: String,
        LastName: String,
        EmailID: String,
        BluetoothID: String) {
        val usermodel=UserModel(UserID, UserName, FirstName, LastName, EmailID, BluetoothID)
        val newUserRequest=service.createUser(usermodel)
        newUserRequest.enqueue(object:Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                Log.d("retrofit",response.message())
                Log.d("retrofit",response.headers().toString())
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("retrofit","failure")
            }

        })
    }
}