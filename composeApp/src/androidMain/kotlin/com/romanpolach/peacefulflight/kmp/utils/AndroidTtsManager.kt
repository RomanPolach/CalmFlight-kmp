package com.romanpolach.peacefulflight.kmp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AndroidTtsManager(
    context: Context,
    private val settingsRepository: SettingsRepository
) : TtsManager {

    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    private val _isInitialized = MutableStateFlow(false)
    private val _availableVoices = MutableStateFlow<List<TtsVoiceOption>>(emptyList())

    override val availableVoices: StateFlow<List<TtsVoiceOption>> = _availableVoices.asStateFlow()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    _isInitialized.value = true
                    refreshAvailableVoices()
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

    private fun refreshAvailableVoices() {
        _availableVoices.value = tts
            ?.voices
            ?.asSequence()
            ?.filter { voice ->
                val locale = voice.locale ?: return@filter false
                locale.language.equals(Locale.ENGLISH.language, ignoreCase = true) &&
                    !voice.isNetworkConnectionRequired
            }
            ?.sortedWith(
                compareBy<Voice> { it.isNetworkConnectionRequired }
                    .thenBy { it.locale?.displayName ?: "" }
                    .thenBy { it.name }
            )
            ?.map { voice ->
                val locale = voice.locale ?: Locale.US
                TtsVoiceOption(
                    id = voice.name,
                    name = locale.displayName.ifBlank { voice.name },
                    localeTag = locale.toLanguageTag()
                )
            }
            ?.toList()
            .orEmpty()
    }

    private fun restoreSavedVoice() {
        val savedVoiceName = settingsRepository.getTtsVoiceName() ?: return
        setVoice(savedVoiceName)
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

    override fun setVoice(voiceId: String) {
        val selectedVoice = tts?.voices?.firstOrNull { it.name == voiceId } ?: return
        tts?.voice = selectedVoice
        settingsRepository.setTtsVoiceName(voiceId)
    }

    override fun setSpeechRate(rate: Float) {
        tts?.setSpeechRate(rate)
        settingsRepository.setTtsSpeechRate(rate)
    }
}
