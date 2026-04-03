package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import com.romanpolach.peacefulflight.kmp.data.weather.WeatherRepository
import com.romanpolach.peacefulflight.kmp.data.weather.WeatherStringKeys
import com.romanpolach.peacefulflight.kmp.model.CockpitUiState
import com.romanpolach.peacefulflight.kmp.model.FlightStatus
import com.romanpolach.peacefulflight.kmp.model.WeatherUiState
import com.romanpolach.peacefulflight.kmp.utils.FlightModeManager
import com.romanpolach.peacefulflight.kmp.utils.Permission
import com.romanpolach.peacefulflight.kmp.utils.PermissionManager
import com.romanpolach.peacefulflight.kmp.utils.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Cockpit screen.
 * Migrated from the original Android app.
 */
class CockpitViewModel(
    private val flightModeManager: FlightModeManager,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository,
    private val locationProvider: com.romanpolach.peacefulflight.kmp.utils.LocationProvider,
    private val permissionManager: PermissionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CockpitUiState())
    val uiState: StateFlow<CockpitUiState> = _uiState.asStateFlow()

    init {
        refreshSettings()
        viewModelScope.launch {
            flightModeManager.isFlightActive.collect { active ->
                _uiState.update { it.copy(isFlightActive = active) }
            }
        }
    }

    fun setStatus(newStatus: FlightStatus) {
        _uiState.update { it.copy(status = newStatus) }
    }

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(weather = WeatherUiState(isLoading = true)) }
            val result = weatherRepository.getWeather(lat, lon)
            _uiState.update { it.copy(weather = result) }
        }
    }

    fun setWeatherError(errorKey: String) {
        _uiState.update {
            it.copy(
                weather = WeatherUiState(
                    isLoading = false,
                    errorKey = errorKey
                )
            )
        }
    }

    fun refreshWeather() {
        _uiState.update { it.copy(weather = null) }
    }

    fun fetchWeatherWithLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(weather = WeatherUiState(isLoading = true)) }

            val permissionState = permissionManager.checkPermission(Permission.LOCATION)
            if (permissionState != PermissionState.GRANTED) {
                val result = permissionManager.requestPermission(Permission.LOCATION)
                if (result != PermissionState.GRANTED) {
                    setWeatherError(WeatherStringKeys.ERROR_LOCATION)
                    return@launch
                }
            }

            val location = locationProvider.getCurrentLocation()
            if (location != null) {
                fetchWeather(location.latitude, location.longitude)
            } else {
                setWeatherError(WeatherStringKeys.ERROR_LOCATION)
            }
        }
    }

    fun toggleSettingsDialog(show: Boolean) {
        _uiState.update { it.copy(showSettingsDialog = show) }
        if (!show) {
            refreshSettings()
        }
    }

    fun refreshSettings() {
        _uiState.update {
            it.copy(
                isMetric = settingsRepository.isMetric(),
                themeMode = settingsRepository.getThemeMode()
            )
        }
    }
}
