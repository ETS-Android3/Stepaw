package com.butterflies.stepaw.scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.butterflies.stepaw.R


class BleConnectionScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_connection_screen)

        val button: Button = findViewById<View>(R.id.next_button) as Button
        button.setOnClickListener {
//            Toast.makeText(applicationContext, "hii there", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@BleConnectionScreen, BleConnectionScreen2::class.java))
        }
    }
}