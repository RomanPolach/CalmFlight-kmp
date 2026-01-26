package com.romanpolach.peacefulflight.kmp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Weather API response from Open-Meteo
 */
@Serializable
data class WeatherResponse(
    @SerialName("current") val current: CurrentWeather
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m") val temperature: Double,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("wind_speed_10m") val windSpeed: Double
)

/**
 * UI state for the cockpit screen
 */
data class CockpitUiState(
    val status: FlightStatus = FlightStatus.BOARDING,
    val weather: WeatherUiState? = null,
    val isMetric: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isFlightActive: Boolean = false
)

/**
 * Weather UI state for display
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val errorKey: String? = null,      // String key instead of @StringRes
    val temperature: Double = 0.0,
    val windSpeed: Double = 0.0,
    val weatherCode: Int = 0,
    val weatherDescription: String = "",
    val passengerMessage: String = "",
    val isGoodForTakeoff: Boolean = true,
    val messageKey: String = "",       // String key for message
    val messageArgs: List<Any> = emptyList(),
    val cityName: String? = null
)

/**
 * Theme mode for the app
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}
