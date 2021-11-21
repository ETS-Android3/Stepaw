package com.butterflies.stepaw.network

import com.butterflies.stepaw.network.models.PetGetModel
import com.butterflies.stepaw.network.models.PetModel
import com.butterflies.stepaw.network.models.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //    Create new user
    @POST("/user/users")
    fun createUser(
        @Header("Authorization") token: String,
        @Body userModel: UserModel
    ): Call<UserModel>

    @GET("/user/users")
    fun getAllUsers(@Header("Authorization") token: String): Call<List<UserModel>>

    @POST("/user/pets")
    fun createPet(@Header("Authorization") token: String, @Body petModel: PetModel): Call<PetModel>

    @GET("/user/pets")
    fun getAllPets(@Header("Authorization") token: String): Call<List<PetGetModel>>

    @GET("/user/pets/{id}")
    fun getPetById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<PetGetModel>

    @PUT("/user/pets/{id}")
    fun updatePetWithId(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<PetGetModel>

    @GET("/user/{id}")
    fun getPersonWithId(@Header("Authorization") token: String,@Path(value = "id") id:String):Call<UserModel>

    companion object {
        const val BASE_URL = "https://stepaw.wmdd4950.com"
    }
}