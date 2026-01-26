package com.romanpolach.peacefulflight.kmp.utils

class IosLocationProvider : LocationProvider {
    override suspend fun getCurrentLocation(): Location? {
        // Platform-specific CoreLocation implementation would go here
        return null
    }
}
