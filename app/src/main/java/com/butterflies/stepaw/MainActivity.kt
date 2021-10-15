package com.butterflies.stepaw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.butterflies.stepaw.authentication.LoginActivity
import com.butterflies.stepaw.authentication.OneTapLogin
import com.butterflies.stepaw.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.send.setOnClickListener {
            val intent=Intent(this,OneTapLogin::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }


}