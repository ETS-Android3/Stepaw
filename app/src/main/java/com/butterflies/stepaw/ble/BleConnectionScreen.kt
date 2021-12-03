package com.butterflies.stepaw.scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.butterflies.stepaw.R
import com.butterflies.stepaw.ble.BleConnectionScreen2


class BleConnectionScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble_connection_screen)

        val button: Button = findViewById<View>(R.id.next_button) as Button
        button.setOnClickListener {
//            Toast.makeText(applicationContext, "hii there", Toast.LENGTH_SHORT).show()
            val petId = intent.getStringExtra("petId");
            val petName = intent.getStringExtra("petName");
            val newIntent = Intent(this, BleConnectionScreen2::class.java).apply {
                putExtra("petId", petId)
                putExtra("petName", petName)
            }
            startActivity(newIntent)
            //startActivity(Intent(this@BleConnectionScreen, BleConnectionScreen3::class.java))
        }
    }
}