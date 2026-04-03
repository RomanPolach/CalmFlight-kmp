package com.romanpolach.peacefulflight.kmp.data.weather

import com.romanpolach.peacefulflight.kmp.model.WeatherResponse
import com.romanpolach.peacefulflight.kmp.model.WeatherUiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException

/**
 * Weather repository - fetches weather data from Open-Meteo API using Ktor
 * Replaces Retrofit-based implementation for multiplatform
 */
class WeatherRepository(
    private val httpClient: HttpClient,
    private val settings: com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
) {
    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/forecast"
    }

    suspend fun getWeather(lat: Double, lon: Double): WeatherUiState {
        return try {
            val response: WeatherResponse = httpClient.get(BASE_URL) {
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter("current", "temperature_2m,weather_code,wind_speed_10m")
            }.body()

            val current = response.current

            val isCalmWeather = current.weatherCode <= 3
            val isLowWind = current.windSpeed < 25.0
            val isGoodForTakeoff = isCalmWeather && isLowWind

            // Parse weather code for detailed information
            val weatherInfo = WeatherCodeParser.getWeatherInfo(current.weatherCode)

            // Build message based on conditions
            val (messageKey, messageArgs) = if (isGoodForTakeoff) {
                WeatherStringKeys.MESSAGE_EXCELLENT to emptyList<Any>()
            } else {
                if (!isLowWind) {
                    val knots = current.windSpeed * 0.539957
                    WeatherStringKeys.MESSAGE_BREEZY to listOf(current.windSpeed, knots)
                } else {
                    WeatherStringKeys.MESSAGE_CLOUDY to emptyList()
                }
            }

            WeatherUiState(
                isLoading = false,
                temperature = current.temperature,
                windSpeed = current.windSpeed,
                weatherCode = current.weatherCode,
                weatherDescription = weatherInfo.description,
                passengerMessage = weatherInfo.passengerMessage,
                isGoodForTakeoff = isGoodForTakeoff,
                messageKey = messageKey,
                messageArgs = messageArgs,
                cityName = null // Geocoding will be platform-specific
            )
        } catch (_: UnresolvedAddressException) {
            WeatherUiState(
                isLoading = false,
                errorKey = WeatherStringKeys.ERROR_OFFLINE
            )
        } catch (_: ConnectTimeoutException) {
            WeatherUiState(
                isLoading = false,
                errorKey = WeatherStringKeys.ERROR_OFFLINE
            )
        } catch (_: SocketTimeoutException) {
            WeatherUiState(
                isLoading = false,
                errorKey = WeatherStringKeys.ERROR_OFFLINE
            )
        } catch (_: IOException) {
            WeatherUiState(
                isLoading = false,
                errorKey = WeatherStringKeys.ERROR_OFFLINE
            )
        } catch (_: Exception) {
            WeatherUiState(
                isLoading = false,
                errorKey = WeatherStringKeys.ERROR_GENERIC
            )
        }
    }
}
