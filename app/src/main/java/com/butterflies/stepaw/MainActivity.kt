package com.butterflies.stepaw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.butterflies.stepaw.authentication.AuthUIHost
import com.butterflies.stepaw.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var token:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val sharedData=getSharedPreferences("com.butterflies.stepaw",Context.MODE_PRIVATE)
        val token=sharedData.getString("com.butterflies.stepaw.idToken","invalid")
        if (token == "invalid") {
            Intent(this, AuthUIHost::class.java).run { startActivity(this) }
        }else{
            if (token != null) {
                Log.d("sharedtoken",token)
            }
        }
//        Intent(this,ChartReport::class.java).also {
//            startActivity(it) }

//        if (token == "invalid") {
//            Intent(this, AuthUIHost::class.java).run { startActivity(this) }
//        } else {
//            Intent(this, ChartReport::class.java).run { startActivity(this) }
//        }
//
    }

}