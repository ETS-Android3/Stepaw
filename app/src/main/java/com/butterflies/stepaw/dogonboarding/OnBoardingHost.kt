package com.butterflies.stepaw.dogonboarding

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.butterflies.stepaw.DogList
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.ActivityOnBoardingHostBinding
import com.butterflies.stepaw.network.ApiService
import com.butterflies.stepaw.network.models.PetModel
import com.butterflies.stepaw.network.models.UserModel
import com.butterflies.stepaw.utils.StepawUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class OnBoardingHost : AppCompatActivity(), AddDogFragment.OnBoardingService,EditDogFragment.OnBoardingService {
    private val PICK_IMAGE_REQUEST = 22
    private lateinit var filePath: Uri
    private lateinit var storage: FirebaseStorage
    var storageReference: StorageReference? = null
    lateinit var idToken: String
    private lateinit var userId: String
    private lateinit var binding: ActivityOnBoardingHostBinding
    private lateinit var imageURL: String;
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val service: ApiService = retrofit.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var idToken: String;

        binding.myImageView.setOnClickListener { selectImage() }
        binding.myImageViewText.setOnClickListener { selectImage() }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.reference;

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.custom_back_button)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Edit"
////        Bottom sheet settings


        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_onboard))
        standardBottomSheetBehavior.halfExpandedRatio=.7F
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        standardBottomSheetBehavior.isDraggable = false


//      setting navhost nav graph programatically
        val navHostFragment: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.findNavController().setGraph(R.navigation.dogonboarding_nav)


        val bundle = intent.extras

        if (bundle?.containsKey("petId") == true) {
//
            // findNavController(R.id.nav_host).navigate(R.id.action_add_Dog_fragment_to_editDogFragment)
            val bundle1 = Bundle();
            bundle1.putString("petId", bundle.getString("petId"))
            navHostFragment.findNavController().navigate(R.id.editDogFragment, bundle1)

        }

    }


    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        );
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode === PICK_IMAGE_REQUEST && resultCode === RESULT_OK && attr.data != null && data.data != null) {

                // Get the Uri of data
                filePath = data.data!!
                try {

//                     Setting image on image view using Bitmap
                    val bitmap = MediaStore.Images.Media
                        .getBitmap(
                            contentResolver,
                            filePath
                        )
                    //                imageView.setImageBitmap(bitmap)
                    Glide.with(this).load(bitmap).into(binding.myImageView)
                    uploadImage()
                } catch (e: IOException) {
                    // Log the exception
                    e.printStackTrace()
                }
            }
        }
    }

    private fun uploadImage() {

        val ref = storageReference
            ?.child(
                "images/"
                        + UUID.randomUUID().toString()
            )

        val uploadTask = ref?.putFile(filePath)
        uploadTask!!.continueWith {
            if (!it.isSuccessful) {
                it.exception?.let { t ->
                    throw t
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result!!.addOnSuccessListener { task ->
                    val myUri = task.toString()
                    imageURL = myUri
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()

        return true
    }

    @SuppressLint("LogNotTimber")
    override fun registerDog(name: String, age: Float, weight: Float, gender: String) {
        val p = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        val token = p.getString("com.butterflies.stepaw.idToken", "invalid")
        var userData = p.getString("com.butterflies.stepaw.user", "invalid")
        if (userData !== "invalid") {
            val gson = Gson()
            val j = gson.fromJson(userData, UserModel::class.java)
            userId = j.UserID
        }
        if (token !== "invalid") {
            if (token != null) {
                idToken = token
            }
        }
        if (!this@OnBoardingHost::imageURL.isInitialized) {
            imageURL =
                "https://firebasestorage.googleapis.com/v0/b/spherical-rune-330820.appspot.com/o/images%2Fdog_placeholder_image.png?alt=media&token=e5cda1d2-b987-4901-9a2e-34d2a437f856"
        }
        if (this::idToken.isInitialized && userId !== "invalid" && this::userId.isInitialized) {
            val petmodel = PetModel(
                "invalid",
                Age = age.toString(),
                Weight = weight.toString(),
                Gender = gender,
                Picture = imageURL,
                NumberOfSteps = "0",
                Distance = "0",
                Duration = "0",
                UserID = userId,
                PetName = name,
                Date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CANADA).format(Date())
            )
            val newPetRequest = service.createPet(token = " Bearer $idToken", petmodel)
            Log.d("newpet", "callback")
            newPetRequest.enqueue(object : Callback<PetModel> {
                override fun onResponse(call: Call<PetModel>, response: Response<PetModel>) {
//
                    if (response.isSuccessful) {
                        Intent(this@OnBoardingHost, DogList::class.java).also { startActivity(it) }
                    }
                    Log.d("newpet", response.message())
                }

                override fun onFailure(call: Call<PetModel>, t: Throwable) {
                    Log.d("newpet", "failed")
                }

            })

        } else {
            Log.d("newpet", "Something was null")
            Log.d("newpet", userId)
            Log.d("newpet", token!!)
        }

    }

    override fun editDog(name: String, age: Float, weight: Float, gender: String) {
        val p = getSharedPreferences("com.butterflies.stepaw", Context.MODE_PRIVATE)
        val token = p.getString("com.butterflies.stepaw.idToken", "invalid")
        var userData = p.getString("com.butterflies.stepaw.user", "invalid")
        if (userData !== "invalid") {
            val gson = Gson()
            val j = gson.fromJson(userData, UserModel::class.java)
            userId = j.UserID
        }
        if (token !== "invalid") {
            if (token != null) {
                idToken = token
            }
        }
        if (!this@OnBoardingHost::imageURL.isInitialized) {
            imageURL =
                "https://firebasestorage.googleapis.com/v0/b/spherical-rune-330820.appspot.com/o/images%2Fdog_placeholder_image.png?alt=media&token=e5cda1d2-b987-4901-9a2e-34d2a437f856"
        }
        if (this::idToken.isInitialized && userId !== "invalid" && this::userId.isInitialized) {
            val petmodel = PetModel(
                "invalid",
                Age = age.toString(),
                Weight = weight.toString(),
                Gender = gender,
                Picture = imageURL,
                NumberOfSteps = "0",
                Distance = "0",
                Duration = "0",
                UserID = userId,
                PetName = name,
                Date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CANADA).format(Date())
            )
//           Update data to backend here.
        }
    }
}