package com.polypote.sentenceoftheday

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.polypote.sentenceoftheday.backend.service.ImageOfTheDayService
import com.polypote.sentenceoftheday.backend.utils.AlarmUtils
import com.polypote.sentenceoftheday.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val DATE_OF_THE_DAY_KEY = "lastOpenDate"
        private const val NOTIFICATION_TIME_KEY = "notificationTime"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide navigation bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            showTimePickerDialog()
        }

        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)

        val (hour, minute) = getNotificationTimeFromPrefs()
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val todayDate = LocalDateTime.now().toString()
        val defValue = "default"
        val lastOpenDate = sharedPrefs.getString(DATE_OF_THE_DAY_KEY, defValue)
        if (lastOpenDate != todayDate || lastOpenDate == defValue) {
            val imageOfTheDayService = ImageOfTheDayService(application)
            imageOfTheDayService.switchImages()
            Log.d("Switch Image", "Images switched")
            sharedPrefs.edit().putString(DATE_OF_THE_DAY_KEY, todayDate).apply()
            lifecycleScope.launch {
                imageOfTheDayService.fetchImageFromUnsplash()
            }
        }
        AlarmUtils(this).initRepeatingAlarm(calendar)
    }

    private fun showTimePickerDialog() {
        val (hour, minute) = getNotificationTimeFromPrefs()
        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val calendar: Calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            this.getPreferences(Context.MODE_PRIVATE).edit().putString(NOTIFICATION_TIME_KEY, "$selectedHour:$selectedMinute").apply()
            AlarmUtils(this).initRepeatingAlarm(calendar)
        }, hour, minute, true).apply {
            setTitle("Heure de la notification")
        }
        timePickerDialog.show()
    }

    private fun getNotificationTimeFromPrefs(): Pair<Int, Int> {
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        val time = sharedPrefs.getString(NOTIFICATION_TIME_KEY, "08:00") ?: "08:00"
        val hourMinute = time.split(":")
        val hour = hourMinute[0].toInt()
        val minute = hourMinute[1].toInt()
        return Pair(hour, minute)
    }
}