package com.butterflies.stepaw.userActions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.butterflies.stepaw.R

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.custom_back_button)
        val toolbarTitle=findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.notifications_title)
    }
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}