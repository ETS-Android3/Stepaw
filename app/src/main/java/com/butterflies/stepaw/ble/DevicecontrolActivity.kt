package com.butterflies.stepaw.ble


import com.butterflies.stepaw.R
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.os.IBinder

import android.util.Log
import android.view.View
import com.butterflies.stepaw.ChartReport
import kotlinx.android.synthetic.main.activity_devicecontrol.*
import org.jetbrains.anko.stopService
import android.content.Intent




class DevicecontrolActivity : AppCompatActivity() {
    private var bluetoothService : BluetoothLeService? = null
    private var deviceAddress: String = ""
    private var connected: Boolean? = null
    private var step: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicecontrol)
        deviceAddress = intent.getStringExtra("address").toString()
        val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
        bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    connected = true
//                    updateConnectionState(R.string.connected)
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    connected = false
                    Log.d("pacho",step.toString());
//                    updateConnectionState(R.string.disconnected)
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    connected = false
                    step = intent.getStringExtra("data")?.toInt()
                    intent.getStringExtra("data")?.let { Log.d("yui", it) }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
        if (bluetoothService != null) {
            val result = bluetoothService!!.connect(deviceAddress)
            Log.d("Request Failed", "Connect request result=$result")
        }

        button.setOnClickListener(View.OnClickListener { view ->
            Log.d("clicked", "onclicked")
            bluetoothService?.disconnect()
        })
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(gattUpdateReceiver)
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
            addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
        }
    }

    // Code to manage Service lifecycle.
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            bluetoothService = (service as BluetoothLeService.LocalBinder).getService()
            bluetoothService?.let { bluetooth ->
                if (!bluetooth.initialize()) {
                    Log.d("Intialization Failed", "Unable to initialize Bluetooth")
                    finish()
                }
                bluetooth.connect(deviceAddress)
//                val intent = Intent(this@DevicecontrolActivity, ChartReport::class.java)
//                intent.putExtra("address", intent.getStringExtra("address").toString())
//                startActivity(intent)
            }

        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            bluetoothService = null
            Log.d("rax", "stopped service");
        }
    }
}