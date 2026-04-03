@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLLocationAccuracyKilometer
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IosLocationProvider : LocationProvider {

    private val locationManager = CLLocationManager().apply {
        desiredAccuracy = kCLLocationAccuracyKilometer
    }

    private var activeDelegate: LocationRequestDelegate? = null

    override suspend fun getCurrentLocation(): Location? = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->
            if (!CLLocationManager.locationServicesEnabled()) {
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            val status = locationManager.authorizationStatus
            if (!status.isAuthorized()) {
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            locationManager.location?.toSharedLocation()?.let { cachedLocation ->
                continuation.resume(cachedLocation)
                return@suspendCancellableCoroutine
            }

            val delegate = LocationRequestDelegate(
                onLocation = { location ->
                    clearDelegate()
                    if (continuation.isActive) {
                        continuation.resume(location)
                    }
                },
                onFailure = {
                    clearDelegate()
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
            )

            activeDelegate = delegate
            locationManager.delegate = delegate
            continuation.invokeOnCancellation { clearDelegate() }
            locationManager.requestLocation()
        }
    }

    private fun clearDelegate() {
        if (locationManager.delegate === activeDelegate) {
            locationManager.delegate = null
        }
        activeDelegate = null
    }
}

private fun CLAuthorizationStatus.isAuthorized(): Boolean {
    return this == kCLAuthorizationStatusAuthorizedWhenInUse ||
        this == kCLAuthorizationStatusAuthorizedAlways
}

private fun CLLocation.toSharedLocation(): Location {
    val coordinate = coordinate.useContents {
        Location(latitude = latitude, longitude = longitude)
    }
    return coordinate
}

private class LocationRequestDelegate(
    private val onLocation: (Location) -> Unit,
    private val onFailure: () -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManager(
        manager: CLLocationManager,
        didUpdateLocations: List<*>
    ) {
        val latestLocation = didUpdateLocations.lastOrNull() as? CLLocation
        val sharedLocation = latestLocation?.toSharedLocation()
        if (sharedLocation != null) {
            onLocation(sharedLocation)
        } else {
            onFailure()
        }
    }

    override fun locationManager(
        manager: CLLocationManager,
        didFailWithError: NSError
    ) {
        onFailure()
    }
}
