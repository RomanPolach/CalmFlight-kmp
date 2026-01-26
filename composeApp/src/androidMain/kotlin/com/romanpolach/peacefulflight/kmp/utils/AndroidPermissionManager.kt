package com.romanpolach.peacefulflight.kmp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.romanpolach.peacefulflight.kmp.MainActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidPermissionManager(private val context: Context) : PermissionManager {

    override suspend fun checkPermission(permission: Permission): PermissionState {
        return when (permission) {
            Permission.LOCATION -> {
                val hasCoarse = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val hasFine = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (hasCoarse || hasFine) PermissionState.GRANTED else PermissionState.DENIED
            }

            Permission.NOTIFICATIONS -> {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    PermissionState.GRANTED
                } else {
                    PermissionState.DENIED
                }
            }
        }
    }

    override suspend fun requestPermission(permission: Permission): PermissionState {
        val activity = MainActivity.getCurrentActivity() ?: return PermissionState.DENIED

        val androidPermission = when (permission) {
            Permission.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
            Permission.NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
        }

        return suspendCancellableCoroutine { continuation ->
            activity.requestPermission(androidPermission) { isGranted ->
                continuation.resume(if (isGranted) PermissionState.GRANTED else PermissionState.DENIED)
            }
        }
    }

    private fun Permission.toAndroidPermission(): String = when (this) {
        Permission.LOCATION -> Manifest.permission.ACCESS_COARSE_LOCATION
        Permission.NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
    }
}
