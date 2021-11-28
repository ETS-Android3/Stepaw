package com.butterflies.stepaw.dogonboarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.butterflies.stepaw.DogList
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.ActivityOnBoardingHostBinding
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.models.PetModel
import com.butterflies.stepaw.network.models.UserModel
import com.butterflies.stepaw.utils.StepawUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class OnBoardingHost : AppCompatActivity(), AddDogFragment.OnBoardingService {
    lateinit var idToken:String
    private lateinit var userId:String
    private lateinit var binding: ActivityOnBoardingHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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

        return true
    }

    @SuppressLint("LogNotTimber")
    override fun registerDog(name: String, age: Float, weight: Float, gender: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)


        val p = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        val token = p.getString("com.butterflies.stepaw.idToken", "invalid")
//        val userId = p.getString("com.butterflies.stepaw.uid", "invalid")
        var userData=p.getString("com.butterflies.stepaw.user","invalid")
        if(userData!=="invalid"){
            val gson=Gson()
            val j=gson.fromJson(userData,UserModel::class.java)
            userId=j.UserID
        }
       if(token!=="invalid"){
           if (token != null) {
               idToken=token
           }
       }
        if (this::idToken.isInitialized && userId !== "invalid"&&this::userId.isInitialized) {
            val petmodel = PetModel(
                "invalid",
                Age = age.toString(),
                Weight = weight.toString(),
                Gender = gender,
                Picture = "not available",
                NumberOfSteps = "0",
                Distance = "0",
                Duration = "0",
                UserID = userId,
                PetName = name,
                Date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CANADA).format(Date())
            )
            val newPetRequest = service.createPet(token = " Bearer $idToken", petmodel)
            Log.d("newpet","callback")
            newPetRequest.enqueue(object : Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
//
                    if(response.isSuccessful){
                        Intent(this@OnBoardingHost,DogList::class.java).also { startActivity(it) }
                    }
                   Log.d("newpet",response.message())
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                  Log.d("newpet","failed")
                }

            })

        } else {
            Log.d("newpet", "Something was null")
            Log.d("newpet",userId)
            Log.d("newpet", token!!)
        }

    }
}