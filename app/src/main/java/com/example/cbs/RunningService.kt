package com.example.cbs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.Timer
import java.util.TimerTask


class RunningService : Service() {
    private var notifTimer: Timer = Timer()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                Actions.START.toString() -> start()
                Actions.STOP.toString() -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        notifTimer.cancel()
        stopSelf()
    }

    private fun start() {
        Toast.makeText(this, "Counter service starting", Toast.LENGTH_SHORT).show()
        var notificationManager: NotificationManager?
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "running_channel",
                "Running Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            // Register the channel with the system.
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else {
            notificationManager = null
        }

        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Service is active")
            .setContentText("Nidday am Counting: 0")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        startForeground(1, notification.build())

        if (notificationManager is NotificationManager) {
            var counter = 1
            notifTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    notification.setContentText("Nidday am Counting: ${counter}")
                    notificationManager.notify(1, notification.build())
                    counter++
                }
            }, 0, 10000)
        }
    }

    enum class Actions {
        START, STOP
    }
}