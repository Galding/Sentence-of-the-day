package com.polypote.sentenceoftheday.backend.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.polypote.sentenceoftheday.backend.receivers.AlarmReceiver
import java.util.Calendar

class AlarmUtils(context: Context) {
    private var _context = context
    private var _alarmManager: AlarmManager? = null
    private var _alarmIntent: PendingIntent

    init {
        _alarmManager = _context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        _alarmIntent = Intent(_context, AlarmReceiver::class.java).let { mIntent ->
            // if you want more than one notification use different requestCode
            // every notification need different requestCode
            PendingIntent.getBroadcast(_context, 100, mIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }

    fun initRepeatingAlarm(calendar: Calendar) {
        _alarmManager?.set(AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            _alarmIntent)
    }
}