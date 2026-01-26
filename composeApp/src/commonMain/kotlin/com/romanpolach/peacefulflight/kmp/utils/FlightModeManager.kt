package com.romanpolach.peacefulflight.kmp.utils

import com.romanpolach.peacefulflight.kmp.data.repository.FlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the flight state (active/inactive) across the app.
 * Migrated from the original Android app.
 */
class FlightModeManager(private val repository: FlightRepository) {

    private val _isFlightActive = MutableStateFlow(false)
    val isFlightActive: StateFlow<Boolean> = _isFlightActive.asStateFlow()

    private var currentFlightId: Long? = null

    suspend fun startFlight(expectedFear: Int) {
        val id = repository.startFlight(expectedFear)
        currentFlightId = id
        _isFlightActive.value = true
    }

    suspend fun endFlight(actualFear: Int) {
        currentFlightId?.let { id ->
            repository.endFlight(id, actualFear)
        }
        currentFlightId = null
        _isFlightActive.value = false
    }

    suspend fun restoreActiveFlightIfExists() {
        val activeFlight = repository.getActiveFlightWithin24Hours()
        if (activeFlight != null) {
            currentFlightId = activeFlight.id
            _isFlightActive.value = true
        }
    }
}
