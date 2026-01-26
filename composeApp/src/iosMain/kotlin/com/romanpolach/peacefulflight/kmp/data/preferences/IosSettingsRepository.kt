package com.romanpolach.peacefulflight.kmp.data.preferences

import com.romanpolach.peacefulflight.kmp.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

class IosSettingsRepository : SettingsRepository {
    private val defaults = NSUserDefaults.standardUserDefaults

    private val _themeModeFlow = MutableStateFlow(getThemeMode())
    override val themeModeFlow: StateFlow<ThemeMode> = _themeModeFlow.asStateFlow()

    override fun getUnitSystem(): UnitSystem {
        val value = defaults.stringForKey("unit_system") ?: UnitSystem.IMPERIAL.name
        return try {
            UnitSystem.valueOf(value)
        } catch (e: Exception) {
            UnitSystem.IMPERIAL
        }
    }

    override fun setUnitSystem(unitSystem: UnitSystem) {
        defaults.setObject(unitSystem.name, "unit_system")
    }

    override fun getThemeMode(): ThemeMode {
        val value = defaults.stringForKey("theme_mode") ?: ThemeMode.SYSTEM.name
        return try {
            ThemeMode.valueOf(value)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    override fun setThemeMode(themeMode: ThemeMode) {
        defaults.setObject(themeMode.name, "theme_mode")
        _themeModeFlow.value = themeMode
    }

    override fun getTtsVoiceName(): String? = defaults.stringForKey("tts_voice")
    override fun setTtsVoiceName(voiceName: String) {
        defaults.setObject(voiceName, "tts_voice")
    }

    override fun getTtsSpeechRate(): Float {
        val rate = defaults.floatForKey("tts_speech_rate")
        return if (rate == 0f) 0.8f else rate
    }

    override fun setTtsSpeechRate(rate: Float) {
        defaults.setFloat(rate, "tts_speech_rate")
    }
}
