package com.polypote.sentenceoftheday.ui.sentence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polypote.sentenceoftheday.backend.database.QuoteService

class SentenceViewModel(application : Application) : AndroidViewModel(application) {
    private val quoteService = QuoteService(application.applicationContext)

    private val _text = MutableLiveData<String>().apply {
        value = quoteService.fetchForTheCurrentDay().body
    }
    val text: LiveData<String> = _text
}