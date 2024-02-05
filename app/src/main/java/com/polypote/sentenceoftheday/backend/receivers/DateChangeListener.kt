package com.polypote.sentenceoftheday.backend.receivers

interface DateChangeListener {
    fun onDateChanged(message : String)
}
