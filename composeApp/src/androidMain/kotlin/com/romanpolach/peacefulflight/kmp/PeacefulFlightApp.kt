package com.romanpolach.peacefulflight.kmp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.romanpolach.peacefulflight.kmp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import java.lang.ref.WeakReference

class PeacefulFlightApp : Application(), Application.ActivityLifecycleCallbacks {
    
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        initKoin {
            androidContext(this@PeacefulFlightApp)
            androidLogger()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity?.get() == activity) {
            currentActivity = null
        }
    }

    companion object {
        private var currentActivity: WeakReference<Activity>? = null
        fun getCurrentActivity(): Activity? = currentActivity?.get()
    }
}
