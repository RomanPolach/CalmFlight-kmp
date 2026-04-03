package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TtsVoiceOption(
    val id: String,
    val name: String,
    val localeTag: String
)

/**
 * Text-to-Speech manager interface for cross-platform usage.
 */
interface TtsManager {
    val availableVoices: StateFlow<List<TtsVoiceOption>>
    fun speak(text: String)
    fun stop()
    fun isSpeaking(): Boolean
    fun setVoice(voiceId: String)
    fun setSpeechRate(rate: Float)
}

/**
 * No-op implementation of TtsManager for platforms where TTS is not yet implemented.
 */
class NoOpTtsManager : TtsManager {
    private val emptyVoices = MutableStateFlow<List<TtsVoiceOption>>(emptyList())

    override val availableVoices: StateFlow<List<TtsVoiceOption>> = emptyVoices.asStateFlow()

    override fun speak(text: String) {
        // Do nothing
    }

    override fun stop() {
        // Do nothing
    }

    override fun isSpeaking(): Boolean = false

    override fun setVoice(voiceId: String) {
        // Do nothing
    }

    override fun setSpeechRate(rate: Float) {
        // Do nothing
    }
}
