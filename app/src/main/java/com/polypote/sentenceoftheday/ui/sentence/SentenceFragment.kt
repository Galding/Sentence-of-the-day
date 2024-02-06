package com.polypote.sentenceoftheday.ui.sentence

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.polypote.sentenceoftheday.backend.receivers.AlarmReceiver
import com.polypote.sentenceoftheday.backend.receivers.DateChangeListener
import com.polypote.sentenceoftheday.databinding.FragmentSentenceBinding

class SentenceFragment : Fragment(), DateChangeListener {
    private var _binding: FragmentSentenceBinding? = null
    private val binding get() = _binding!!

    private lateinit var hourSetByUserReachedReceiver : AlarmReceiver


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sentenceViewModel =
            ViewModelProvider(this).get(SentenceViewModel::class.java)

        _binding = FragmentSentenceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSentence
        sentenceViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        hourSetByUserReachedReceiver = AlarmReceiver()
        IntentFilter(Intent.ACTION_DATE_CHANGED).also {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(hourSetByUserReachedReceiver, it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(hourSetByUserReachedReceiver)
    }

    override fun onDateChanged(message: String) {
        TODO("Update UI with the message")
    }
}