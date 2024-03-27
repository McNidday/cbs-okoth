package com.example.cbs

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.TextView


class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val btState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            val apm = (context as Activity).findViewById<View>(R.id.apm) as TextView
            when (btState) {
                BluetoothAdapter.STATE_OFF -> apm.text = ("Bluetooth is OFF Nidday")
                BluetoothAdapter.STATE_TURNING_OFF -> apm.text = ("Bluetooth is turning OFF Nidday")
                BluetoothAdapter.STATE_ON -> apm.text = ("Bluetooth is ON Nidday")
                BluetoothAdapter.STATE_TURNING_ON -> apm.text = ("Bluetooth is turning ON Nidday")
            }
        }
    }
}