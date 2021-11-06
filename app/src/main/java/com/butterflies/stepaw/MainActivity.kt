package com.butterflies.stepaw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.butterflies.stepaw.WelcomeScreen.WelcomeScreenHost
import com.butterflies.stepaw.authentication.AuthUIHost
import com.butterflies.stepaw.authentication.OneTapLogin
import com.butterflies.stepaw.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        binding.send.setOnClickListener {
            val intent = Intent(this, OneTapLogin::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val intent = Intent(this, AuthUIHost::class.java)
            startActivity(intent)
        }
        binding.welcomeText.setOnClickListener { Intent(this,WelcomeScreenHost::class.java).run { startActivity(this) } }
       binding.button2.setOnClickListener { Intent(this,Reminder::class.java).also { startActivity(it) } }
    }



}