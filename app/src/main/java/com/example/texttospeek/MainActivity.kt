package com.example.texttospeek

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.texttospeek.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityMainBinding
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize TextToSpeech
        tts = TextToSpeech(this, this)

        // Disable speak button initially
        binding.speekBtn.isEnabled = false

        // Set click listeners for buttons to adjust speech rate
        binding.speed05Btn.setOnClickListener { setSpeechRate(0.5f) }
        binding.speed10Btn.setOnClickListener { setSpeechRate(1.0f) }
        binding.speed15Btn.setOnClickListener { setSpeechRate(1.5f) }
        binding.speed2Btn.setOnClickListener { setSpeechRate(2.0f) }

        // Speak button click listener
        binding.speekBtn.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("ur"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                binding.speekBtn.isEnabled = true
                // Set default speech rate
                setSpeechRate(1.0f)
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun setSpeechRate(rate: Float) {
        tts?.setSpeechRate(rate)
        binding.custumSpeedTxt.setText("Speed:  "+rate.toString())
    }

    private fun speakOut() {
        val text = binding.editTxt.text.toString()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        // Shutdown TTS when activity is destroyed
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
