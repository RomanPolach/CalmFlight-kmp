package com.romanpolach.peacefulflight.kmp.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class AndroidLocationProvider(private val context: Context) : LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        return try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            // Try last known location first
            val lastLocation = fusedLocationClient.lastLocation.await()
            if (lastLocation != null) {
                return Location(lastLocation.latitude, lastLocation.longitude)
            }

            // Request fresh location
            val cts = CancellationTokenSource()
            val freshLocation = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cts.token
            ).await()

            freshLocation?.let { Location(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }
}
