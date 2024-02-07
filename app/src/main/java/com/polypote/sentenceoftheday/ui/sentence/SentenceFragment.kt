package com.polypote.sentenceoftheday.ui.sentence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.polypote.sentenceoftheday.databinding.FragmentSentenceBinding

class SentenceFragment : Fragment() {
    private var _binding: FragmentSentenceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sentenceViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[SentenceViewModel::class.java]

        _binding = FragmentSentenceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSentence
        sentenceViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}