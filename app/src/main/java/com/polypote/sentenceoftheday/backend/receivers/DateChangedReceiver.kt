package com.polypote.sentenceoftheday.backend.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DateChangedReceiver(private val listener: DateChangeListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = "mockString"
        listener.onDateChanged(message)
    }
}