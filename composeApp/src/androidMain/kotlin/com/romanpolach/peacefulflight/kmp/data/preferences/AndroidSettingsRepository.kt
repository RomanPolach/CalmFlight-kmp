package com.romanpolach.peacefulflight.kmp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.romanpolach.peacefulflight.kmp.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidSettingsRepository(context: Context) : SettingsRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("calm_flight_prefs", Context.MODE_PRIVATE)

    private val _themeModeFlow = MutableStateFlow(getThemeMode())
    override val themeModeFlow: StateFlow<ThemeMode> = _themeModeFlow.asStateFlow()

    override fun getUnitSystem(): UnitSystem {
        val value = prefs.getString("unit_system", UnitSystem.IMPERIAL.name)
        return try {
            UnitSystem.valueOf(value!!)
        } catch (e: Exception) {
            UnitSystem.IMPERIAL
        }
    }

    override fun setUnitSystem(unitSystem: UnitSystem) {
        prefs.edit().putString("unit_system", unitSystem.name).apply()
    }

    override fun getThemeMode(): ThemeMode {
        val value = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(value!!)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    override fun setThemeMode(themeMode: ThemeMode) {
        prefs.edit().putString("theme_mode", themeMode.name).apply()
        _themeModeFlow.value = themeMode
    }

    override fun getTtsVoiceName(): String? = prefs.getString("tts_voice", null)
    override fun setTtsVoiceName(voiceName: String) {
        prefs.edit().putString("tts_voice", voiceName).apply()
    }

    override fun getTtsSpeechRate(): Float = prefs.getFloat("tts_speech_rate", 0.8f)
    override fun setTtsSpeechRate(rate: Float) {
        prefs.edit().putFloat("tts_speech_rate", rate).apply()
    }
}
