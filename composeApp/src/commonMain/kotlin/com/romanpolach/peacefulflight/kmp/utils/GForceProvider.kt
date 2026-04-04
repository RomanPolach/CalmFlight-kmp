package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for platform-specific G-Force tracking
 */
interface GForceProvider {
    val gForceHistory: StateFlow<List<Float>>
    val currentGForce: StateFlow<Float>
    val isSensorAvailable: StateFlow<Boolean>

    fun startTracking()
    fun stopTracking()
}

/**
 * Status categories for G-Force levels
 */
enum class GForceStatus {
    SMOOTH,
    LIGHT_BUMPS,
    MODERATE,
    BUMPY
}
