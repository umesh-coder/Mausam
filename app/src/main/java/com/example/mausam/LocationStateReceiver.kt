package com.example.mausam

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class LocationStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.location.PROVIDERS_CHANGED") {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (!isLocationEnabled) {
                Toast.makeText(context, "Location turned off", Toast.LENGTH_SHORT).show()
                notifyUserLocationOff(context)
                // Handle location turned off

            }else{
                // Handle location turned on
                Toast.makeText(context, "Location turned on", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun notifyUserLocationOff(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for the location notification.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location_channel",
                "Location",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification.
        val notification = NotificationCompat.Builder(context, "location_channel")
            .setContentTitle("Location Off")
            .setContentText("Please turn on location to use this app.")
            .setSmallIcon(androidx.core.R.drawable.notify_panel_notification_icon_bg)
            .build()

        // Show the notification.
        notificationManager.notify(1, notification)
    }
}