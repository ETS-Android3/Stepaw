package com.butterflies.stepaw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.butterflies.stepaw.authentication.AuthUIHost
import com.butterflies.stepaw.databinding.ActivityMainBinding
import com.butterflies.stepaw.scanner.BleActivity
import com.butterflies.stepaw.scanner.BleConnectionScreen
import com.butterflies.stepaw.welcomescreen.WelcomeScreenHost


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
        val userJson= sharedData.getString("com.butterflies.stepaw.user", "invalid")
        val firstTimeUser=sharedData.getString("com.butterflies.stepaw.firstTimeUser","true")
//        Intent(this,BleConnectionScreen::class.java).also { startActivity(it) }
        if(firstTimeUser=="true"){
            Intent(this,WelcomeScreenHost::class.java).also { startActivity(it) }
        }
      else if(token!=="invalid"&&userJson!=="invalid"&&userJson!==null){
          Intent(this, DogList::class.java).run { startActivity(this) }
      }else{
          Intent(this,AuthUIHost::class.java).run { startActivity(this) }
      }
    }

}