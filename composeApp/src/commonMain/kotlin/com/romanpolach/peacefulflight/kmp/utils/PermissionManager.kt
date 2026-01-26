package com.romanpolach.peacefulflight.kmp.utils

/**
 * Supported permission types
 */
enum class Permission {
    LOCATION,
    NOTIFICATIONS
}

/**
 * Current state of a permission
 */
enum class PermissionState {
    GRANTED,
    DENIED,
    NOT_DETERMINED,
    DENIED_ALWAYS // Android "Don't ask again" or iOS Manual Settings required
}

/**
 * Interface for platform-specific permission handling
 */
interface PermissionManager {
    suspend fun checkPermission(permission: Permission): PermissionState
    suspend fun requestPermission(permission: Permission): PermissionState
}
