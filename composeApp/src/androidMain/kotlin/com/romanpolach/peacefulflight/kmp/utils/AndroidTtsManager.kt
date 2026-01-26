package com.romanpolach.peacefulflight.kmp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

class AndroidTtsManager(
    context: Context,
    private val settingsRepository: SettingsRepository
) : TtsManager {

    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    private val _isInitialized = MutableStateFlow(false)

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    _isInitialized.value = true
                    restoreSavedVoice()
                    restoreSavedSpeechRate()

                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            _isSpeaking.value = true
                        }

                        override fun onDone(utteranceId: String?) {
                            _isSpeaking.value = false
                        }

                        override fun onError(utteranceId: String?) {
                            _isSpeaking.value = false
                        }
                    })
                }
            }
        }
    }

    private fun restoreSavedVoice() {
        val savedVoiceName = settingsRepository.getTtsVoiceName() ?: return
        val voices = tts?.voices ?: return
        val savedVoice = voices.find { it.name == savedVoiceName }
        if (savedVoice != null) {
            tts?.voice = savedVoice
        }
    }

    private fun restoreSavedSpeechRate() {
        val savedRate = settingsRepository.getTtsSpeechRate()
        tts?.setSpeechRate(savedRate)
    }

    override fun speak(text: String) {
        if (_isInitialized.value) {
            stop()
            val paragraphs = text.split("\n\n").filter { it.isNotBlank() }
            val params = android.os.Bundle()

            paragraphs.forEachIndexed { index, paragraph ->
                val queueMode = if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
                tts?.speak(paragraph.trim(), queueMode, params, "TTS_ID_$index")

                if (index < paragraphs.size - 1) {
                    tts?.playSilentUtterance(1000L, TextToSpeech.QUEUE_ADD, "PAUSE_$index")
                }
            }
        }
    }

    override fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    override fun isSpeaking(): Boolean = _isSpeaking.value
}
