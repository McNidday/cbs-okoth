package com.example.cbs

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private val connectivityReceiver = ConnectivityReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContentView(R.layout.activity_main)
        val btFilter = IntentFilter()
        btFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
//        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        btFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(connectivityReceiver, btFilter)



        // Start counting service
        Intent(applicationContext, RunningService::class.java).also {
            it.action = RunningService.Actions.START.toString()
            startService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        Intent(applicationContext, RunningService::class.java).also {
            it.action = RunningService.Actions.STOP.toString()
            startService(it)
        }
    }
}