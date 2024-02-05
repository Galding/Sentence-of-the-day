package com.polypote.sentenceoftheday.ui.sentence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SentenceViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "\"La vague va passer, et va fermer tous les PC\"\nSamir Aknine"
    }
    val text: LiveData<String> = _text
}