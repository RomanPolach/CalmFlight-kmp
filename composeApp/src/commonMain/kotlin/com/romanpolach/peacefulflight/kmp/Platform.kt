package com.romanpolach.peacefulflight.kmp

/**
 * Platform interface for platform-specific implementations
 */
interface Platform {
    val name: String
}

/**
 * Get the current platform
 */
expect fun getPlatform(): Platform