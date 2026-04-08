package com.romanpolach.peacefulflight.kmp.utils

/**
 * Resolves a human-readable city or region name from coordinates.
 */
interface CityNameResolver {
    suspend fun getCityName(latitude: Double, longitude: Double): String?
}
