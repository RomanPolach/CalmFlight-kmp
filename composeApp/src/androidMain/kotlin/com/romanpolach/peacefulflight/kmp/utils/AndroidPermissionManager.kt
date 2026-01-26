package com.romanpolach.peacefulflight.kmp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.romanpolach.peacefulflight.kmp.MainActivity
import com.romanpolach.peacefulflight.kmp.PeacefulFlightApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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

    override suspend fun requestPermission(permission: Permission): PermissionState =
        withContext(Dispatchers.Main) {
            val activity = PeacefulFlightApp.getCurrentActivity() as? MainActivity
                ?: return@withContext PermissionState.DENIED

            return@withContext when (permission) {
                Permission.LOCATION -> {
                    val permissions = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    suspendCancellableCoroutine { continuation ->
                        activity.requestMultiplePermissions(permissions) { result ->
                            val isGranted = result.values.any { it }
                            continuation.resume(if (isGranted) PermissionState.GRANTED else PermissionState.DENIED)
                        }
                    }
                }

                Permission.NOTIFICATIONS -> {
                    val androidPermission = Manifest.permission.POST_NOTIFICATIONS
                    suspendCancellableCoroutine { continuation ->
                        activity.requestPermission(androidPermission) { isGranted ->
                            continuation.resume(if (isGranted) PermissionState.GRANTED else PermissionState.DENIED)
                        }
                    }
                }
            }
    }
}
