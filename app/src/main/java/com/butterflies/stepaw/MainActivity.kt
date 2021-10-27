package com.butterflies.stepaw

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.butterflies.stepaw.authentication.Signin
import com.butterflies.stepaw.authentication.OneTapLogin
import com.butterflies.stepaw.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        binding.send.setOnClickListener {
            val intent = Intent(this, OneTapLogin::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
       binding.button2.setOnClickListener { Intent(this,Reminder::class.java).also { startActivity(it) } }
    }



}