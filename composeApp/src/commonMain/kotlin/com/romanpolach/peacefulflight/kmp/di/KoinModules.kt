package com.romanpolach.peacefulflight.kmp.di

import com.romanpolach.peacefulflight.kmp.data.repository.FlightRepository
import com.romanpolach.peacefulflight.kmp.data.weather.WeatherRepository
import com.romanpolach.peacefulflight.kmp.utils.FlightModeManager
import com.romanpolach.peacefulflight.kmp.viewmodel.CockpitViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.GForceViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.GuidedInterventionViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.LearnViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.MainViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.RidingTheWaveViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.RealityCheckViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.SosViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.ToolsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Common Koin module for shared dependencies
 */
val commonModule = module {
    // Network
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }

    // Utilities
    single { FlightModeManager(get()) }

    // Repositories
    single { WeatherRepository(get(), get(), get()) }
    single { FlightRepository(get()) }

    // ViewModels
    factory { MainViewModel(get(), get()) }
    factory { CockpitViewModel(get(), get(), get(), get(), get()) }
    factory { LearnViewModel() }
    factory { SosViewModel() }
    factory { ToolsViewModel() }
    factory { GForceViewModel(get()) }
    factory { RidingTheWaveViewModel(get()) }
    factory { GuidedInterventionViewModel(get()) }
    factory { RealityCheckViewModel(get()) }
}

/**
 * Platform-specific module aggregator
 */
expect fun platformModule(): Module
