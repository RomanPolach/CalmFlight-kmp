@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark
import platform.Foundation.NSError
import kotlin.coroutines.resume

class IosCityNameResolver : CityNameResolver {

    private val geocoder = CLGeocoder()

    override suspend fun getCityName(latitude: Double, longitude: Double): String? =
        withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val location = CLLocation(latitude = latitude, longitude = longitude)

                geocoder.reverseGeocodeLocation(location) { placemarks, error: NSError? ->
                    if (!continuation.isActive) {
                        return@reverseGeocodeLocation
                    }

                    val placemark = placemarks?.firstOrNull() as? CLPlacemark
                    val cityName = if (error == null) {
                        placemark?.locality
                            ?: placemark?.subAdministrativeArea
                            ?: placemark?.administrativeArea
                    } else {
                        null
                    }

                    continuation.resume(cityName)
                }

                continuation.invokeOnCancellation {
                    geocoder.cancelGeocode()
                }
            }
        }
}
