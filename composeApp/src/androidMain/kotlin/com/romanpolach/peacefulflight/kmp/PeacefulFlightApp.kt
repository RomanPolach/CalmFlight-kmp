package com.romanpolach.peacefulflight.kmp

import android.app.Application
import com.romanpolach.peacefulflight.kmp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class PeacefulFlightApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@PeacefulFlightApp)
            androidLogger()
        }
    }
}
