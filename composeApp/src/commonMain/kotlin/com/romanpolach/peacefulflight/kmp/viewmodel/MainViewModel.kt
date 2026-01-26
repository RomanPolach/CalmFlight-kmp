package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import com.romanpolach.peacefulflight.kmp.utils.FlightModeManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Main application ViewModel.
 * Migrated from the original Android app.
 */
class MainViewModel(
    private val flightModeManager: FlightModeManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val themeMode = settingsRepository.themeModeFlow
    val isFlightActive: StateFlow<Boolean> = flightModeManager.isFlightActive

    init {
        viewModelScope.launch {
            flightModeManager.restoreActiveFlightIfExists()
        }
    }

    fun startFlight(expectedFear: Int) {
        viewModelScope.launch {
            flightModeManager.startFlight(expectedFear)
        }
    }

    fun endFlight(actualFear: Int) {
        viewModelScope.launch {
            flightModeManager.endFlight(actualFear)
        }
    }
}
