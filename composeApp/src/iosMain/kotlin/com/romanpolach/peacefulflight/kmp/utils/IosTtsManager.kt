package com.romanpolach.peacefulflight.kmp.utils

import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.AVFAudio.AVSpeechBoundary
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechSynthesizerDelegateProtocol
import platform.AVFAudio.AVSpeechUtterance
import platform.AVFAudio.AVSpeechUtteranceDefaultSpeechRate
import platform.AVFAudio.AVSpeechUtteranceMaximumSpeechRate
import platform.AVFAudio.AVSpeechUtteranceMinimumSpeechRate
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class IosTtsManager(
    private val settingsRepository: SettingsRepository
) : NSObject(), TtsManager, AVSpeechSynthesizerDelegateProtocol {

    private val synthesizer = AVSpeechSynthesizer()
    private val _availableVoices = MutableStateFlow<List<TtsVoiceOption>>(emptyList())
    private val _isSpeaking = MutableStateFlow(false)

    override val availableVoices: StateFlow<List<TtsVoiceOption>> = _availableVoices.asStateFlow()

    private var selectedVoiceId: String? = null
    private var speechRate: Float = settingsRepository.getTtsSpeechRate()

    init {
        synthesizer.delegate = this
        refreshAvailableVoices()
        restoreSavedVoice()
    }

    override fun speak(text: String) {
        val paragraphs = text
            .split("\n\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (paragraphs.isEmpty()) {
            return
        }

        stop()

        val voice = resolveVoice()
        val iosRate = speechRate.toIosSpeechRate()

        paragraphs.forEach { paragraph ->
            val utterance = AVSpeechUtterance(string = paragraph)
            utterance.voice = voice
            utterance.rate = iosRate
            utterance.postUtteranceDelay = 0.35
            synthesizer.speakUtterance(utterance)
        }
    }

    override fun stop() {
        if (synthesizer.speaking) {
            synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
        }
        _isSpeaking.value = false
    }

    override fun isSpeaking(): Boolean = synthesizer.speaking

    override fun setVoice(voiceId: String) {
        val voiceExists = _availableVoices.value.any { it.id == voiceId }
        if (!voiceExists) {
            return
        }

        selectedVoiceId = voiceId
        settingsRepository.setTtsVoiceName(voiceId)
    }

    override fun setSpeechRate(rate: Float) {
        speechRate = rate.coerceIn(0.5f, 1.5f)
        settingsRepository.setTtsSpeechRate(speechRate)
    }

    @ObjCSignatureOverride
    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        didStartSpeechUtterance: AVSpeechUtterance
    ) {
        _isSpeaking.value = true
    }

    @ObjCSignatureOverride
    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        didFinishSpeechUtterance: AVSpeechUtterance
    ) {
        if (!synthesizer.speaking) {
            _isSpeaking.value = false
        }
    }

    @ObjCSignatureOverride
    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        didCancelSpeechUtterance: AVSpeechUtterance
    ) {
        _isSpeaking.value = false
    }

    private fun refreshAvailableVoices() {
        _availableVoices.value = AVSpeechSynthesisVoice
            .speechVoices()
            .mapNotNull { it as? AVSpeechSynthesisVoice }
            .filter { voice -> voice.language.startsWith("en", ignoreCase = true) }
            .sortedWith(compareBy({ it.language }, { it.name }))
            .map { voice ->
                TtsVoiceOption(
                    id = voice.identifier,
                    name = voice.name,
                    localeTag = voice.language
                )
            }
    }

    private fun restoreSavedVoice() {
        val savedVoice = settingsRepository.getTtsVoiceName()
        val defaultVoice = _availableVoices.value.firstOrNull()?.id
        selectedVoiceId = savedVoice
            ?.takeIf { id -> _availableVoices.value.any { it.id == id } }
            ?: defaultVoice
    }

    private fun resolveVoice(): AVSpeechSynthesisVoice? {
        val preferredId = selectedVoiceId
        return when {
            preferredId != null -> AVSpeechSynthesisVoice.voiceWithIdentifier(preferredId)
            else -> AVSpeechSynthesisVoice.voiceWithLanguage("en-US")
        }
    }
}

private fun Float.toIosSpeechRate(): Float {
    val scaledRate = AVSpeechUtteranceDefaultSpeechRate * this
    return scaledRate.coerceIn(
        AVSpeechUtteranceMinimumSpeechRate,
        AVSpeechUtteranceMaximumSpeechRate
    )
}
