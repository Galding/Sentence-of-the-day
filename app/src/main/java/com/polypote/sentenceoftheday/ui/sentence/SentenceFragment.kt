package com.polypote.sentenceoftheday.ui.sentence

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.polypote.sentenceoftheday.R
import com.polypote.sentenceoftheday.databinding.FragmentSentenceBinding
import java.io.File

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

        val background : ImageView = binding.image
        sentenceViewModel.background.observe(viewLifecycleOwner){
            Log.d("Sentence fragment ", it)
            if(it != ""){
                background.setImageBitmap(decodeSampledBitmapFromResource(it, background.width, background.height))
            }

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

    fun decodeSampledBitmapFromResource(
        file: String,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeFile(file)
        }
    }
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}

