package com.polypote.sentenceoftheday.backend.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.polypote.sentenceoftheday.backend.utils.NotificationUtils


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationUtils = NotificationUtils(context!!)
        notificationUtils.launchNotification()
    }
}