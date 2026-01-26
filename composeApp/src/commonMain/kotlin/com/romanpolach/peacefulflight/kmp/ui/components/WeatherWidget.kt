package com.romanpolach.peacefulflight.kmp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romanpolach.peacefulflight.kmp.model.WeatherUiState
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.weather_check_btn
import peacefulflight.composeapp.generated.resources.weather_error_generic
import peacefulflight.composeapp.generated.resources.weather_error_location
import peacefulflight.composeapp.generated.resources.weather_retry_btn
import peacefulflight.composeapp.generated.resources.weather_widget_title

@Composable
fun WeatherWidget(
    weatherState: WeatherUiState?,
    onFetchClick: () -> Unit,
    onRetry: () -> Unit,
    isMetric: Boolean
) {
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.weather_widget_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (weatherState?.cityName != null) {
                        Text(
                            text = weatherState.cityName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                if (weatherState != null && !weatherState.isLoading && weatherState.errorKey == null) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = getWeatherIcon(weatherState.weatherCode),
                        contentDescription = null,
                        tint = getWeatherIconTint(weatherState.weatherCode)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (weatherState == null) {
                Button(
                    onClick = onFetchClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(Res.string.weather_check_btn))
                }
            } else if (weatherState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else if (weatherState.errorKey != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Cloud,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            resolveErrorString(
                                weatherState.errorKey ?: "weather_error_generic"
                            )
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(stringResource(Res.string.weather_retry_btn))
                    }
                }
            } else {
                // Weather Data Display
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val temp =
                        if (isMetric) "${weatherState.temperature.toInt()}°C" else "${(weatherState.temperature * 9 / 5 + 32).toInt()}°F"
                    Text(
                        text = temp,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        if (weatherState.weatherDescription.isNotEmpty()) {
                            Text(
                                text = weatherState.weatherDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        val windSpeedText =
                            if (isMetric) "${weatherState.windSpeed.toInt()} km/h" else "${(weatherState.windSpeed * 0.621371).toInt()} mph"
                        Text(
                            text = "Wind: $windSpeedText",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Passenger Message
                if (weatherState.passengerMessage.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (weatherState.isGoodForTakeoff)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else
                                    MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = weatherState.passengerMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun resolveErrorString(key: String): StringResource {
    return when (key) {
        "weather_error_location" -> Res.string.weather_error_location
        "weather_error_generic" -> Res.string.weather_error_generic
        else -> Res.string.weather_error_generic
    }
}

@Composable
private fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0, 1 -> Icons.Default.WbSunny
        else -> Icons.Default.Cloud
    }
}

@Composable
private fun getWeatherIconTint(code: Int) = when (code) {
    0, 1 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.primary
}
