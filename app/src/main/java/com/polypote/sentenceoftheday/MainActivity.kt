package com.polypote.sentenceoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.polypote.sentenceoftheday.backend.utils.AlarmUtils
import com.polypote.sentenceoftheday.databinding.ActivityMainBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 59)
        }

        AlarmUtils(this).initRepeatingAlarm(calendar)
    }
}