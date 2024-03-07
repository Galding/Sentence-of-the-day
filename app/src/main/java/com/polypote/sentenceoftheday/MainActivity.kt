package com.polypote.sentenceoftheday

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.polypote.sentenceoftheday.backend.service.ImageOfTheDayService
import com.polypote.sentenceoftheday.backend.utils.AlarmUtils
import com.polypote.sentenceoftheday.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.time.LocalDateTime
import java.util.Calendar
import java.util.prefs.Preferences


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val dateOfTheDayKey = "lastOpenDate"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 59)
        }

        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        val todayDate = LocalDateTime.now().toString()
        val defValue = "default"
        val lastOpenDate = sharedPrefs.getString(dateOfTheDayKey, defValue)
        if(lastOpenDate != todayDate || lastOpenDate == defValue){
            val imageOfTheDayService = ImageOfTheDayService(application)
            imageOfTheDayService.switchImages()
            Log.d("Switch Image", "Images switched")
            sharedPrefs.edit().putString(dateOfTheDayKey, todayDate).apply()
            lifecycleScope.launch {
                imageOfTheDayService.fetchImageFromUnsplash()
            }
        }
        AlarmUtils(this).initRepeatingAlarm(calendar)
    }
}