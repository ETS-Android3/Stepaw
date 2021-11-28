package com.butterflies.stepaw.ble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.butterflies.stepaw.R
import org.jetbrains.anko.alert

private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val SERVICE_DATA_UUID = "0000183E-0000-1000-8000-00805f9b34fb"
private const val LOCATION_PERMISSION_REQUEST_CODE = 2

private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
private var scanning = false
private val handler = Handler()

// Stops scanning after 10 seconds.
private const val SCAN_PERIOD: Long = 5000

private val filter = ScanFilter.Builder().setServiceUuid(
    ParcelUuid.fromString(SERVICE_DATA_UUID)
).build()

private var filters = ArrayList<ScanFilter>()

class DeviceScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_scan)
        val devicesListView = findViewById<View>(R.id.devicesListView) as ListView
        val arrayAdapter: ArrayAdapter<BluetoothDevice> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1)
        val button = findViewById<Button>(R.id.scan) as Button
        devicesListView.setAdapter(arrayAdapter)

        if (bluetoothAdapter == null) {
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }

        val leScanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                val exist = arrayAdapter.getPosition(result.device).toString()
                if (exist == "-1") {
                    arrayAdapter.add(result.device)
                    arrayAdapter.notifyDataSetChanged()
                }
            }
        }

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        fun scanLeDevice() {
            if (!scanning) {
                handler.postDelayed({
                    scanning = false
                    bluetoothLeScanner?.stopScan(leScanCallback)
                    button.text = "Start Scan"
                    arrayAdapter.clear()
                    arrayAdapter.notifyDataSetChanged()
                }, SCAN_PERIOD)
                scanning = true
                filters.add(filter)
                bluetoothLeScanner?.startScan(filters, scanSettings, leScanCallback)
            } else {
                scanning = false
                bluetoothLeScanner?.stopScan(leScanCallback)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted) {
            requestLocationPermission()
        } else {
            arrayAdapter.clear()
            arrayAdapter.notifyDataSetChanged()
        }

        button.setOnClickListener {
            button.text = if (button.text.toString() == "Start Scan") "Stop Scan" else "Start Scan"
            if (button.text.toString() == "Stop Scan") {
                arrayAdapter.clear()
                arrayAdapter.notifyDataSetChanged()
                scanLeDevice()
            }
        }

        devicesListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val value = devicesListView.getItemAtPosition(position).toString()
            val intent = Intent(this@DeviceScanActivity, DevicecontrolActivity::class.java)
            intent.putExtra("address", value)
            startActivity(intent)
        })
    }

    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun requestLocationPermission() {
        if (isLocationPermissionGranted) {
            return
        }
        runOnUiThread {
            alert {
                title = "Location permission required"
                message = "Starting from Android M (6.0), the system requires apps to be granted " +
                        "location access in order to scan for BLE devices."
                isCancelable = false
                positiveButton(android.R.string.ok) {
                    requestPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }.show()
        }
    }

    private fun Context.hasPermission(permissionType: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun Activity.requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}