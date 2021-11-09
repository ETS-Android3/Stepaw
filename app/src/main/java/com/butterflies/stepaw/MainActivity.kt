package com.butterflies.stepaw

import android.content.Context
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
//        setSupportActionBar(findViewById(R.id.my_toolbar)
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("com.butterflies.stepaw.idToken", "invalid")
        if (token == "invalid") {
            Intent(this, AuthUIHost::class.java).run { startActivity(this) }
        } else {
            Intent(this, ChartReport::class.java).run { startActivity(this) }
        }

    }


}