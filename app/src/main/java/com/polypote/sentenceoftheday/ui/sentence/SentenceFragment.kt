package com.polypote.sentenceoftheday.ui.sentence

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
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

        val quoteTextView: TextView = binding.textQuote
        sentenceViewModel.quote.observe(viewLifecycleOwner) {
            quoteTextView.text = addBackground(it)
        }

        val authorTextView: TextView = binding.textAuthor
        sentenceViewModel.author.observe(viewLifecycleOwner) {
            authorTextView.text = addBackground(it)
        }

        return root
    }

    private fun addBackground(text: String) : SpannableString {
        val spannable = SpannableString(text)
        spannable.setSpan(
            BackgroundColorSpan(Color.parseColor("#CC808080")),
            0,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}