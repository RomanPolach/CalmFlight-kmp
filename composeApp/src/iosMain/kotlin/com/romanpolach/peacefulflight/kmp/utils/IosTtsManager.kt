package com.romanpolach.peacefulflight.kmp.utils

import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryOptionDuckOthers
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioSessionModeSpokenAudio
import platform.AVFAudio.AVSpeechBoundary
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechSynthesizerDelegateProtocol
import platform.AVFAudio.AVSpeechUtterance
import platform.AVFAudio.AVSpeechUtteranceDefaultSpeechRate
import platform.AVFAudio.AVSpeechUtteranceMaximumSpeechRate
import platform.AVFAudio.AVSpeechUtteranceMinimumSpeechRate
import kotlinx.cinterop.ObjCSignatureOverride
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class IosTtsManager(
    private val settingsRepository: SettingsRepository
) : TtsManager {

    private val synthesizer = AVSpeechSynthesizer()
    private val _availableVoices = MutableStateFlow<List<TtsVoiceOption>>(emptyList())
    private val _isSpeaking = MutableStateFlow(false)
    private val speechDelegate = SpeechDelegate(
        onStart = { _isSpeaking.value = true },
        onFinish = { _isSpeaking.value = false },
        onCancel = { _isSpeaking.value = false }
    )

    override val availableVoices: StateFlow<List<TtsVoiceOption>> = _availableVoices.asStateFlow()

    private var selectedVoiceId: String? = null
    private var speechRate: Float = settingsRepository.getTtsSpeechRate()

    init {
        configureAudioSession()
        synthesizer.delegate = speechDelegate
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
        configureAudioSession()

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

    private fun refreshAvailableVoices() {
        _availableVoices.value = AVSpeechSynthesisVoice
            .speechVoices()
            .mapNotNull { it as? AVSpeechSynthesisVoice }
            .filter { voice -> voice.language.startsWith("en", ignoreCase = true) }
            .sortedWith(
                compareBy<AVSpeechSynthesisVoice>(
                    { voice -> voice.language },
                    { voice -> voice.name }
                )
            )
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

    private fun configureAudioSession() {
        runCatching {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(
                category = AVAudioSessionCategoryPlayback,
                mode = AVAudioSessionModeSpokenAudio,
                options = AVAudioSessionCategoryOptionDuckOthers,
                error = null
            )
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

@OptIn(ExperimentalForeignApi::class)
private class SpeechDelegate(
    private val onStart: () -> Unit,
    private val onFinish: () -> Unit,
    private val onCancel: () -> Unit
) : NSObject(), AVSpeechSynthesizerDelegateProtocol {

    @ObjCSignatureOverride
    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        didStartSpeechUtterance: AVSpeechUtterance
    ) {
        onStart()
    }

    @ObjCSignatureOverride
    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        didFinishSpeechUtterance: AVSpeechUtterance
    ) {
        if (!synthesizer.speaking) {
            onFinish()
        }
    }

    @ObjCSignatureOverride
    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        didCancelSpeechUtterance: AVSpeechUtterance
    ) {
        onCancel()
    }
}
