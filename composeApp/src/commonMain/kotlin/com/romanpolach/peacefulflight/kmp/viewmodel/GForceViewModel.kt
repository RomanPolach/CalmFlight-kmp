package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romanpolach.peacefulflight.kmp.utils.GForceProvider
import com.romanpolach.peacefulflight.kmp.utils.GForceStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

data class GForceUiState(
    val currentGForce: Float = 1.0f,
    val displayedGForce: Float = 1.0f,
    val minReading: Float = 1.0f,
    val maxReading: Float = 1.0f,
    val status: GForceStatus = GForceStatus.SMOOTH,
    val history: List<Float> = emptyList()
)

class GForceViewModel(
    private val gForceProvider: GForceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(GForceUiState())
    val uiState: StateFlow<GForceUiState> = _uiState.asStateFlow()

    private val recentStatuses = MutableStateFlow<List<GForceStatus>>(emptyList())
    private var initialized = false

    init {
        viewModelScope.launch {
            gForceProvider.currentGForce.collect { g ->
                updateGForce(g)
            }
        }

        viewModelScope.launch {
            gForceProvider.gForceHistory.collect { history ->
                _uiState.update { it.copy(history = history) }
            }
        }

        // Stability timer (peak hold effect for display)
        viewModelScope.launch {
            while (true) {
                delay(500L)
                val history = _uiState.value.history
                if (history.isNotEmpty()) {
                    val recentCount = minOf(history.size, 10)
                    val recentHistory = history.takeLast(recentCount)
                    val maxDeviation = recentHistory.maxByOrNull { abs(it - 1.0f) } ?: 1.0f
                    _uiState.update { it.copy(displayedGForce = maxDeviation) }
                }
            }
        }
    }

    private fun updateGForce(currentG: Float) {
        _uiState.update { state ->
            val newMin = if (!initialized) currentG else minOf(state.minReading, currentG)
            val newMax = if (!initialized) currentG else maxOf(state.maxReading, currentG)
            initialized = true

            val deviation = abs(currentG - 1.0f)
            val newStatus = when {
                deviation <= 0.03f -> GForceStatus.SMOOTH
                deviation <= 0.07f -> GForceStatus.LIGHT_BUMPS
                deviation <= 0.13f -> GForceStatus.MODERATE
                else -> GForceStatus.BUMPY
            }

            // Update status stability
            val updatedRecent = (recentStatuses.value + newStatus).takeLast(150)
            recentStatuses.value = updatedRecent

            val stableStatus = when {
                updatedRecent.contains(GForceStatus.BUMPY) -> GForceStatus.BUMPY
                updatedRecent.contains(GForceStatus.MODERATE) -> GForceStatus.MODERATE
                updatedRecent.contains(GForceStatus.LIGHT_BUMPS) -> GForceStatus.LIGHT_BUMPS
                else -> GForceStatus.SMOOTH
            }

            state.copy(
                currentGForce = currentG,
                minReading = newMin,
                maxReading = newMax,
                status = stableStatus
            )
        }
    }

    fun startTracking() {
        gForceProvider.startTracking()
    }

    fun stopTracking() {
        gForceProvider.stopTracking()
    }

    override fun onCleared() {
        super.onCleared()
        gForceProvider.stopTracking()
    }
}
