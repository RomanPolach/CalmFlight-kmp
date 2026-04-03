package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IosPermissionManager : PermissionManager {

    private val locationManager = CLLocationManager()
    private var authorizationDelegate: LocationDelegate? = null

    override suspend fun checkPermission(permission: Permission): PermissionState {
        return when (permission) {
            Permission.LOCATION -> {
                if (!CLLocationManager.locationServicesEnabled()) {
                    PermissionState.DENIED
                } else {
                    val status = locationManager.authorizationStatus
                    mapStatus(status)
                }
            }

            else -> PermissionState.GRANTED
        }
    }

    override suspend fun requestPermission(permission: Permission): PermissionState {
        return when (permission) {
            Permission.LOCATION -> withContext(Dispatchers.Main) {
                if (!CLLocationManager.locationServicesEnabled()) {
                    return@withContext PermissionState.DENIED
                }

                val currentStatus = locationManager.authorizationStatus
                if (currentStatus != kCLAuthorizationStatusNotDetermined) {
                    return@withContext mapStatus(currentStatus)
                }

                suspendCancellableCoroutine { continuation ->
                    val delegate = LocationDelegate { status ->
                        clearDelegate()
                        if (continuation.isActive) {
                            continuation.resume(mapStatus(status))
                        }
                    }

                    authorizationDelegate = delegate
                    locationManager.delegate = delegate
                    continuation.invokeOnCancellation { clearDelegate() }
                    locationManager.requestWhenInUseAuthorization()
                }
            }

            else -> PermissionState.GRANTED
        }
    }

    private fun clearDelegate() {
        if (locationManager.delegate === authorizationDelegate) {
            locationManager.delegate = null
        }
        authorizationDelegate = null
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
