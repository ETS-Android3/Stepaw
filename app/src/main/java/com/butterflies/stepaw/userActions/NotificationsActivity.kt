package com.butterflies.stepaw.userActions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.butterflies.stepaw.R

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title="Notifications"
    }
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}