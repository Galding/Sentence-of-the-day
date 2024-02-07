package com.polypote.sentenceoftheday.backend.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.polypote.sentenceoftheday.backend.database.DBManagerService
import com.polypote.sentenceoftheday.backend.utils.NotificationUtils


class AlarmReceiver(private val dateChangeListener: DateChangeListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationUtils = NotificationUtils(context!!)
        notificationUtils.launchNotification()

        val dbManagerService = DBManagerService(context)
        val cursor = dbManagerService.fetchForTheCurrentDay()

        val bodyPosition = cursor.getColumnIndex("body")
        val message = cursor.getString(bodyPosition)

        dateChangeListener.onDateChanged(message)
        cursor.close()
    }
}