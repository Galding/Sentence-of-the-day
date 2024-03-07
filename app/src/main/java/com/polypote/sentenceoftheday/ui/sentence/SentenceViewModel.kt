package com.polypote.sentenceoftheday.ui.sentence

import android.app.Application
import android.content.Intent
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polypote.sentenceoftheday.R
import com.polypote.sentenceoftheday.backend.database.QuoteService
import com.polypote.sentenceoftheday.backend.service.ImageOfTheDayService
import java.io.File

class SentenceViewModel(application : Application) : AndroidViewModel(application) {
    private val quoteService = QuoteService(application.applicationContext)

    private val _quoteText = MutableLiveData<String>().apply {
        value = quoteService.fetchForTheCurrentDay().body
    }

    private val _author = MutableLiveData<String>().apply {
        value = quoteService.fetchForTheCurrentDay().author
    }

    private val imageOfTheDayService = ImageOfTheDayService(application)
    private val bgImagePath = imageOfTheDayService.getImageOfTheDay()
    private val _background = MutableLiveData<String>().apply {

        value = bgImagePath
    }


    val quote: LiveData<String> = _quoteText
    val author: LiveData<String> = _author
    val background: LiveData<String> = _background
}