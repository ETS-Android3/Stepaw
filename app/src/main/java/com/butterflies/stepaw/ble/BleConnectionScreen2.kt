package com.butterflies.stepaw.ble

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.butterflies.stepaw.R
import android.graphics.drawable.Drawable
import android.os.Handler
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.butterflies.stepaw.databinding.ActivityBleConnectionScreen2Binding


//private lateinit var binding: ActivityBleConnectionScreen2Binding
private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1

class BleConnectionScreen2 : AppCompatActivity() {

    private var petId = ""; private var petName = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBleConnectionScreen2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val imageViewTarget = DrawableImageViewTarget(binding.bleStatusGif)
        Glide.with(this).load(R.raw.bluetooth_status).into<Target<Drawable>>(imageViewTarget)

        petId = intent.getStringExtra("petId").toString()
        petName = intent.getStringExtra("petName").toString()
    }

    override fun onResume() {
        super.onResume()
        if (!bluetoothAdapter.isEnabled) {
            promptEnableBluetooth()
        } else {
            NextActivity()
        }
    }

    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    private fun NextActivity() {
        Handler().postDelayed(Runnable {
            val mainIntent = Intent(this@BleConnectionScreen2, DeviceScanActivity::class.java)
            mainIntent.putExtra("petId", petId)
            mainIntent.putExtra("petName", petName)
            this@BleConnectionScreen2.startActivity(mainIntent)
            this@BleConnectionScreen2.finish()
        }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ENABLE_BLUETOOTH_REQUEST_CODE -> {
                if (resultCode != Activity.RESULT_OK) {
                    promptEnableBluetooth()
                } else {
                    NextActivity()
                    Toast.makeText(this@BleConnectionScreen2, "Value selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
}