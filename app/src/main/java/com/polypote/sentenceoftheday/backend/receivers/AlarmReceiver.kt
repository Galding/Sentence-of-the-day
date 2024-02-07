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
        val body = cursor.getString(cursor.getColumnIndex("body"))
        val author = cursor.getString(cursor.getColumnIndex("author"))
        dateChangeListener.onDateChanged("$body : $author")
        cursor.close()
    }
}