package com.romanpolach.peacefulflight.kmp.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class AndroidLocationProvider(private val context: Context) : LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        return try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            // Try to get fresh location with a timeout to prevent hanging UI
            withTimeoutOrNull(5000) {
                val cts = CancellationTokenSource()
                val freshLocation = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    cts.token
                ).await()

                freshLocation?.let { Location(it.latitude, it.longitude) }
            } ?: run {
                // If fresh location times out, try last known location as fallback
                val lastLocation = fusedLocationClient.lastLocation.await()
                lastLocation?.let { Location(it.latitude, it.longitude) }
            }
        } catch (e: Exception) {
            null
        }
    }
}
