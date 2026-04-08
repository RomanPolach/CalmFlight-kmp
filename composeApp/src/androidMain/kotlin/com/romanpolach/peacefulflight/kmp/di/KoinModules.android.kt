package com.romanpolach.peacefulflight.kmp.di

import com.romanpolach.peacefulflight.kmp.data.local.AppDatabase
import com.romanpolach.peacefulflight.kmp.data.local.RoomBuilder
import com.romanpolach.peacefulflight.kmp.data.preferences.AndroidSettingsRepository
import com.romanpolach.peacefulflight.kmp.data.preferences.SettingsRepository
import com.romanpolach.peacefulflight.kmp.utils.AndroidGForceProvider
import com.romanpolach.peacefulflight.kmp.utils.AndroidCityNameResolver
import com.romanpolach.peacefulflight.kmp.utils.AndroidLocationProvider
import com.romanpolach.peacefulflight.kmp.utils.CityNameResolver
import com.romanpolach.peacefulflight.kmp.utils.AndroidPermissionManager
import com.romanpolach.peacefulflight.kmp.utils.GForceProvider
import com.romanpolach.peacefulflight.kmp.utils.LocationProvider
import com.romanpolach.peacefulflight.kmp.utils.PermissionManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    // Database
    single<AppDatabase> {
        RoomBuilder(get()).builder().build()
    }

    // Preferences
    single<SettingsRepository> { AndroidSettingsRepository(get()) }

    // Location
    single<LocationProvider> { AndroidLocationProvider(get()) }
    single<CityNameResolver> { AndroidCityNameResolver(get()) }

    // Permissions
    single<PermissionManager> { AndroidPermissionManager(get()) }

    // G-Force
    single<GForceProvider> { AndroidGForceProvider(get()) }

    // TTS
    single<com.romanpolach.peacefulflight.kmp.utils.TtsManager> {
        com.romanpolach.peacefulflight.kmp.utils.AndroidTtsManager(get(), get())
    }
}
