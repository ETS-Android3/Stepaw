package com.butterflies.stepaw.scanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.butterflies.stepaw.ChartReport
import com.butterflies.stepaw.R
import com.butterflies.stepaw.ble.DeviceScanActivity

class BleConnectionScreen3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_connection_screen3)
        val intent = intent
        val deviceAddress = intent.getStringExtra("address")
        val petId = intent.getStringExtra("petId")
        Handler().postDelayed(Runnable {
            val mainIntent = Intent(this@BleConnectionScreen3, ChartReport::class.java)
            mainIntent.putExtra("address", deviceAddress)
            mainIntent.putExtra("petId", petId)
            startActivity(mainIntent)
            this@BleConnectionScreen3.finish()
        }, 2000)
    }
}