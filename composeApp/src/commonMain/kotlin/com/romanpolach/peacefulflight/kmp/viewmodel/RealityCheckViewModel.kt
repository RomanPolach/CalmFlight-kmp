package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanpolach.peacefulflight.kmp.data.local.FlightSession
import com.romanpolach.peacefulflight.kmp.data.repository.FlightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.jetbrains.compose.resources.StringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.rc_insight_realistic_msg
import peacefulflight.composeapp.generated.resources.rc_insight_realistic_title
import peacefulflight.composeapp.generated.resources.rc_insight_rough_msg
import peacefulflight.composeapp.generated.resources.rc_insight_rough_title
import peacefulflight.composeapp.generated.resources.rc_insight_strong_msg
import peacefulflight.composeapp.generated.resources.rc_insight_strong_title
import peacefulflight.composeapp.generated.resources.rc_insight_surprised_msg
import peacefulflight.composeapp.generated.resources.rc_insight_surprised_title

data class RealityCheckInsight(
    val titleRes: StringResource,
    val messageRes: StringResource
)

data class RealityCheckUiState(
    val flights: List<FlightSession> = emptyList(),
    val averageDifference: Double = 0.0,
    val insight: RealityCheckInsight? = null
)

class RealityCheckViewModel(
    flightRepository: FlightRepository
) : ViewModel() {

    val uiState: StateFlow<RealityCheckUiState> = flightRepository.allFlights
        .map { flights ->
            val completedFlights = flights
                .filter { it.actualFear != null }
                .sortedBy { it.startTime }

            val recentFlights = completedFlights.takeLast(10)
            val averageDifference = getAverageDifference(recentFlights)

            RealityCheckUiState(
                flights = recentFlights,
                averageDifference = averageDifference,
                insight = completedFlights.takeIf { it.isNotEmpty() }?.let {
                    buildInsight(averageDifference)
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RealityCheckUiState()
        )

    private fun buildInsight(averageDifference: Double): RealityCheckInsight {
        return when {
            averageDifference > 1.5 -> RealityCheckInsight(
                titleRes = Res.string.rc_insight_strong_title,
                messageRes = Res.string.rc_insight_strong_msg
            )

            averageDifference > 0.5 -> RealityCheckInsight(
                titleRes = Res.string.rc_insight_surprised_title,
                messageRes = Res.string.rc_insight_surprised_msg
            )

            averageDifference >= -0.5 -> RealityCheckInsight(
                titleRes = Res.string.rc_insight_realistic_title,
                messageRes = Res.string.rc_insight_realistic_msg
            )

            else -> RealityCheckInsight(
                titleRes = Res.string.rc_insight_rough_title,
                messageRes = Res.string.rc_insight_rough_msg
            )
        }
    }

    private fun getAverageDifference(flights: List<FlightSession>): Double {
        if (flights.isEmpty()) {
            return 0.0
        }

        val sum = flights.sumOf { it.expectedFear - (it.actualFear ?: 0) }
        return sum.toDouble() / flights.size
    }
}
