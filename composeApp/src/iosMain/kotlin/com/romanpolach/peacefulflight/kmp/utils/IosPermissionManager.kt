package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.coroutines.CompletableDeferred
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

class IosPermissionManager : PermissionManager {

    private val locationManager = CLLocationManager()

    override suspend fun checkPermission(permission: Permission): PermissionState {
        return when (permission) {
            Permission.LOCATION -> {
                val status = locationManager.authorizationStatus
                mapStatus(status)
            }

            else -> PermissionState.GRANTED
        }
    }

    override suspend fun requestPermission(permission: Permission): PermissionState {
        return when (permission) {
            Permission.LOCATION -> {
                val currentStatus = locationManager.authorizationStatus
                if (currentStatus != kCLAuthorizationStatusNotDetermined) {
                    return mapStatus(currentStatus)
                }

                val deferred = CompletableDeferred<PermissionState>()
                val delegate = LocationDelegate { status ->
                    deferred.complete(mapStatus(status))
                }

                locationManager.delegate = delegate
                locationManager.requestWhenInUseAuthorization()

                val result = deferred.await()
                locationManager.delegate = null
                result
            }

            else -> PermissionState.GRANTED
        }
    }

    private fun mapStatus(status: CLAuthorizationStatus): PermissionState {
        return when (status) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> PermissionState.GRANTED

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> PermissionState.DENIED

            kCLAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
            else -> PermissionState.DENIED
        }
    }
}

private class LocationDelegate(
    private val onStatusChange: (CLAuthorizationStatus) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {
    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        if (didChangeAuthorizationStatus != kCLAuthorizationStatusNotDetermined) {
            onStatusChange(didChangeAuthorizationStatus)
        }
    }

    // For iOS 14+
    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        val status = manager.authorizationStatus
        if (status != kCLAuthorizationStatusNotDetermined) {
            onStatusChange(status)
        }
    }
}
