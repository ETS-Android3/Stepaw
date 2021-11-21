package com.butterflies.stepaw.userActions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.butterflies.stepaw.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Account : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        auth = Firebase.auth
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Account"
    }
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}