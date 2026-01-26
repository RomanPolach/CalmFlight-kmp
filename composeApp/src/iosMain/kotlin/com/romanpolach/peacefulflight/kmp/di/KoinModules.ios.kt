package com.romanpolach.peacefulflight.kmp.di

import com.romanpolach.peacefulflight.kmp.data.local.AppDatabase
import com.romanpolach.peacefulflight.kmp.data.local.RoomBuilder
import com.romanpolach.peacefulflight.kmp.data.preferences.IosSettingsRepository
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import com.romanpolach.peacefulflight.kmp.utils.GForceProvider
import com.romanpolach.peacefulflight.kmp.utils.IosGForceProvider
import com.romanpolach.peacefulflight.kmp.utils.IosLocationProvider
import com.romanpolach.peacefulflight.kmp.utils.IosPermissionManager
import com.romanpolach.peacefulflight.kmp.utils.LocationProvider
import com.romanpolach.peacefulflight.kmp.utils.PermissionManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    // Database
    single<AppDatabase> {
        RoomBuilder().builder().build()
    }

    // Preferences
    single<SettingsRepository> { IosSettingsRepository() }

    // Location
    single<LocationProvider> { IosLocationProvider() }

    // Permissions
    single<PermissionManager> { IosPermissionManager() }

    // G-Force
    single<GForceProvider> { IosGForceProvider() }
}
