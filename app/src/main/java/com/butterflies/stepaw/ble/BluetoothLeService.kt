package com.butterflies.stepaw.ble

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.bluetooth.*
import android.os.Binder
import android.util.Log
import java.util.*
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothGattCharacteristic
import android.nfc.NfcAdapter.EXTRA_DATA
import androidx.localbroadcastmanager.content.LocalBroadcastManager


internal class BluetoothLeService : Service() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var connectionState = BluetoothProfile.STATE_DISCONNECTED
    private var ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
    private var serviceuuid: UUID = UUID.fromString("0000183E-0000-1000-8000-00805f9b34fb")
    private var characteristicuuid: UUID = UUID.fromString("00002713-0000-1000-8000-00805f9b34fb")
    private var descriptoruuid: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    private var _callback : ((Int) -> Unit)? = null
    private var startTime: Long? = null


    fun setCallback(callback: (Int) -> Unit) {
        _callback = callback
    }

    override fun onCreate() {
        super.onCreate()
        startTime = System.currentTimeMillis();
        Log.d("time", startTime.toString())
    }

    fun initialize(): Boolean {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Log.e("testing", "Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }

    fun connect(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
                return true
            } catch (exception: IllegalArgumentException) {
                Log.d("TAG", "Device not found with provided address.")
                return false
            }
            // connect to the GATT server on the device
        } ?: run {
            Log.d("TAG", "BluetoothAdapter not initialized")
            return false
        }
    }

    fun getRunningTimeMillis(): Long {
        return (System.currentTimeMillis() - startTime!!)
    }


    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                broadcastUpdate(ACTION_GATT_CONNECTED)
                connectionState = STATE_CONNECTED
                // Attempts to discover services after successful connection.
                bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
                connectionState = STATE_DISCONNECTED
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val gattServices: List<BluetoothGattService> = gatt!!.getServices()
                for (gattService in gattServices) {
                    if(gattService.uuid == serviceuuid) {
                        setCharacteristicNotification(gattService.characteristics[0], true)
                    }
                }
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
            } else {
                Log.d("Discovered", "onServicesDiscovered received: $status")
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
//            Log.d("testing", characteristic.getIntValue
//                (BluetoothGattCharacteristic.FORMAT_UINT16, 0).toString())
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
        }
    }

    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {

        bluetoothGatt?.let { gatt ->
            gatt.setCharacteristicNotification(characteristic, enabled)
            if (characteristicuuid == characteristic.uuid) {
                val descriptor = characteristic.getDescriptor(descriptoruuid)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)
            }
        } ?: run {
            Log.w("Read failed", "BluetoothGatt not initialized")
        }
    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)
        when (characteristic.uuid) {
            characteristicuuid -> {
                val flag = characteristic.properties
                val format = when (flag and 0x01) {
                    0x01 -> {
                        BluetoothGattCharacteristic.FORMAT_UINT16
                    }
                    else -> {
                        BluetoothGattCharacteristic.FORMAT_UINT8
                    }
                }
                val step = characteristic.getIntValue(format, 0)
//                Log.d("bledata", String.format("Received rate: %d", step))
//                _callback?.invoke(step)
                intent.putExtra("data", (step).toString())
                intent.putExtra("runtime", getRunningTimeMillis().toString())
                sendBroadcast(intent)
            }
            else -> {
            }
        }
        sendBroadcast(intent)
    }

    private val binder: Binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        close()
        return super.onUnbind(intent)
    }

    private fun close() {
        Log.d("exited", "ertd")
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    companion object {
        const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTED = 2
    }

    internal inner class LocalBinder : Binder() {
        fun getService(): BluetoothLeService {
            return this@BluetoothLeService
        }
    }
}