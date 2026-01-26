package com.romanpolach.peacefulflight.kmp.data.preferences

import com.romanpolach.peacefulflight.kmp.model.ThemeMode
import kotlinx.coroutines.flow.StateFlow

/**
 * Unit system preference
 */
enum class UnitSystem {
    METRIC,      // Celsius, km/h
    IMPERIAL     // Fahrenheit, mph
}

/**
 * Settings/Preferences interface for cross-platform storage
 */
interface SettingsRepository {

    // Unit system
    fun getUnitSystem(): UnitSystem
    fun setUnitSystem(unitSystem: UnitSystem)
    fun isMetric(): Boolean = getUnitSystem() == UnitSystem.METRIC
    fun isImperial(): Boolean = getUnitSystem() == UnitSystem.IMPERIAL

    // Theme mode
    fun getThemeMode(): ThemeMode
    fun setThemeMode(themeMode: ThemeMode)
    val themeModeFlow: StateFlow<ThemeMode>

    // TTS settings
    fun getTtsVoiceName(): String?
    fun setTtsVoiceName(voiceName: String)
    fun getTtsSpeechRate(): Float
    fun setTtsSpeechRate(rate: Float)

    companion object {
        const val DEFAULT_SPEECH_RATE = 0.8f
    }
}
