package com.butterflies.stepaw.dogonboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.butterflies.stepaw.DogList
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.ActivityOnBoardingHostBinding
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.models.PetModel
import com.butterflies.stepaw.network.models.UserModel
import com.butterflies.stepaw.network.networkCall.NetworkCall
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception

class OnBoardingHost : AppCompatActivity(), Add_Dog_fragment.OnBoardingService {
    private lateinit var binding: ActivityOnBoardingHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var idToken: String;


//


//        setSupportActionBar(findViewById(R.id.my_toolbar))
////        supportActionBar?.setHomeButtonEnabled(true)
////        supportActionBar?.setDisplayHomeAsUpEnabled(true)
////        Bottom sheet settings


        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_onboard))
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        standardBottomSheetBehavior.isDraggable = false


//      setting navhost nav graph programatically
        val navHostFragment: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.findNavController().setGraph(R.navigation.dogonboarding_nav)
    }

    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()
        Toast.makeText(this, "Back pressed", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun registerDog(name: String, age: Float, weight: Float, gender: String) {


        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)

        var idToken: String? = null
        val p = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        val token = p.getString("com.butterflies.stepaw.idToken", "invalid")
        val userId = p.getString("com.butterflies.stepaw.uid", "invalid")
        if (token != null) {
            idToken = token
        }
        if (idToken !== null && userId !== "invalid"&&userId!==null) {
            val petmodel = PetModel(
                "invalid",
                Age = age.toString(),
                Weight = weight.toString(),
                Gender = gender,
                Picture = "not available",
                NumberOfSteps = "0",
                Distance = "0",
                Duration = "0",
                UserID = userId
            )
            val newPetRequest = service.createPet(token = " Bearer $idToken", petmodel)
            newPetRequest.enqueue(object : Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
                    Log.d("newpet",response.isSuccessful.toString())
                    if(response.isSuccessful) {
                        Intent(applicationContext,DogList::class.java).also {
                            startActivity(it) }
                    }
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("newpet", t.message.toString())
                    Toast.makeText(this@OnBoardingHost,t.message.toString(),Toast.LENGTH_SHORT).show()

                }

            })

        }else{
            Log.d("newpet","Something was null")
        }

    }
}