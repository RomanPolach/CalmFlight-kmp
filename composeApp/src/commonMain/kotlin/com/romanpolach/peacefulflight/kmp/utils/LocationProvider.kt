package com.romanpolach.peacefulflight.kmp.utils

/**
 * Platform-agnostic location data
 */
data class Location(val latitude: Double, val longitude: Double)

/**
 * Interface for platform-specific location provider
 */
interface LocationProvider {
    suspend fun getCurrentLocation(): Location?
}
