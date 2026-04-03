package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.data.local.FlightSession
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import com.romanpolach.peacefulflight.kmp.viewmodel.RealityCheckViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.rc_avg_accurate
import peacefulflight.composeapp.generated.resources.rc_avg_overestimate
import peacefulflight.composeapp.generated.resources.rc_avg_underestimate
import peacefulflight.composeapp.generated.resources.rc_catastrophe_gap_desc
import peacefulflight.composeapp.generated.resources.rc_empty_desc
import peacefulflight.composeapp.generated.resources.rc_empty_title
import peacefulflight.composeapp.generated.resources.rc_flight_history
import peacefulflight.composeapp.generated.resources.rc_legend_actual
import peacefulflight.composeapp.generated.resources.rc_legend_expected
import peacefulflight.composeapp.generated.resources.rc_reality_gap_desc
import peacefulflight.composeapp.generated.resources.rc_title
import kotlin.math.abs
import kotlin.math.round

@OptIn(KoinExperimentalAPI::class)
@Composable
fun RealityCheckScreen(
    onBackClick: () -> Unit,
    viewModel: RealityCheckViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            StandardTopBar(
                title = stringResource(Res.string.rc_title),
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            if (uiState.flights.isEmpty()) {
                EmptyRealityCheckCard()
            } else {
                RealityCheckInsightCard(
                    averageDifference = uiState.averageDifference,
                    title = uiState.insight?.titleRes?.let { stringResource(it) }.orEmpty(),
                    message = uiState.insight?.messageRes?.let { stringResource(it) }.orEmpty()
                )

                FlightHistoryCard(flights = uiState.flights)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EmptyRealityCheckCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.rc_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.rc_empty_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RealityCheckInsightCard(
    averageDifference: Double,
    title: String,
    message: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            val summaryText = when {
                averageDifference > 0.1 -> stringResource(
                    Res.string.rc_avg_overestimate,
                    roundToSingleDecimal(averageDifference)
                )

                averageDifference < -0.1 -> stringResource(
                    Res.string.rc_avg_underestimate,
                    roundToSingleDecimal(abs(averageDifference))
                )

                else -> stringResource(Res.string.rc_avg_accurate)
            }

            val detailText = if (averageDifference > 0.1) {
                stringResource(Res.string.rc_catastrophe_gap_desc)
            } else {
                stringResource(Res.string.rc_reality_gap_desc)
            }

            Text(
                text = summaryText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = detailText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun FlightHistoryCard(flights: List<FlightSession>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = stringResource(Res.string.rc_flight_history),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendDot(MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.rc_legend_expected),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(16.dp))

                LegendDot(MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.rc_legend_actual),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            flights.forEach { flight ->
                FlightComparisonRow(flight = flight)
            }
        }
    }
}

@Composable
private fun FlightComparisonRow(flight: FlightSession) {
    val actualFear = flight.actualFear ?: return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = formatFlightDate(flight.startTime),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )

        ScoreBar(
            label = "Expected ${flight.expectedFear}/10",
            progress = flight.expectedFear / 10f,
            barColor = MaterialTheme.colorScheme.error
        )

        ScoreBar(
            label = "Actual $actualFear/10",
            progress = actualFear / 10f,
            barColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ScoreBar(
    label: String,
    progress: Float,
    barColor: androidx.compose.ui.graphics.Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(12.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
private fun LegendDot(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .width(12.dp)
            .height(12.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(color)
    )
}

private fun formatFlightDate(startTime: Long): String {
    val dateTime = Instant.fromEpochMilliseconds(startTime)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val month = dateTime.monthNumber.toString().padStart(2, '0')
    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    return "$day/$month/${dateTime.year}"
}

private fun roundToSingleDecimal(value: Double): Double {
    return round(value * 10.0) / 10.0
}
