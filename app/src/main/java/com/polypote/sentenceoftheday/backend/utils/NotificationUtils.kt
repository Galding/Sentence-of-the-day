package com.polypote.sentenceoftheday.backend.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.polypote.sentenceoftheday.MainActivity

class NotificationUtils(private var _context: Context) {
    private lateinit var _notificationBuilder: NotificationCompat.Builder
    private val _notificationManager = NotificationManagerCompat.from(_context)
    private val CHANNEL_ID = "My_Notification_Channel"


    init {
        createNotificationChannel()
        initNotificationBuilder()
    }


    @SuppressLint("MissingPermission")
    fun launchNotification(){
        _notificationManager.notify(0, _notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                _context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun initNotificationBuilder() {

        // Create an explicit intent for an Activity in your app
        val sampleIntent = Intent(_context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(_context, 0, sampleIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        /***
         * Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID.
         * This is required for compatibility with Android 8.0 (API level 26) and higher,
         * but is ignored by older versions.
         */
        _notificationBuilder = NotificationCompat.Builder(_context, CHANNEL_ID)
            //.setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Notification Title")
            .setContentText("Notification Body Text, Notification Body Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            // Automatically removes the notification when the user taps it.
            .setAutoCancel(true)
    }

}