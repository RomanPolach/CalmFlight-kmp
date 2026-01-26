package com.romanpolach.peacefulflight.kmp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

/**
 * Shared initialization point for Koin
 */
fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}): KoinApplication {
    return startKoin {
        appDeclaration()
        modules(commonModule + platformModule())
    }
}
