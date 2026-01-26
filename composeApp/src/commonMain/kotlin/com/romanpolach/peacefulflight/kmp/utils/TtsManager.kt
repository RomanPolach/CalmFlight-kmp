package com.romanpolach.peacefulflight.kmp.utils

/**
 * Text-to-Speech manager interface for cross-platform usage.
 */
interface TtsManager {
    fun speak(text: String)
    fun stop()
    fun isSpeaking(): Boolean
}

/**
 * No-op implementation of TtsManager for platforms where TTS is not yet implemented.
 */
class NoOpTtsManager : TtsManager {
    override fun speak(text: String) {
        // Do nothing
    }

    override fun stop() {
        // Do nothing
    }

    override fun isSpeaking(): Boolean = false
}
