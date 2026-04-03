package com.romanpolach.peacefulflight.kmp.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import kotlin.math.sqrt

@OptIn(ExperimentalForeignApi::class)
class IosGForceProvider : GForceProvider {

    private val _currentGForce = MutableStateFlow(1.0f)
    override val currentGForce: StateFlow<Float> = _currentGForce.asStateFlow()

    private val _gForceHistory = MutableStateFlow<List<Float>>(emptyList())
    override val gForceHistory: StateFlow<List<Float>> = _gForceHistory.asStateFlow()

    private val motionManager = CMMotionManager()
    private var smoothedValue = 9.81f
    private val alpha = 0.05f
    private val maxHistorySize = 300

    override fun startTracking() {
        if (!motionManager.accelerometerAvailable || motionManager.accelerometerActive) {
            return
        }

        motionManager.accelerometerUpdateInterval = 1.0 / 30.0
        motionManager.startAccelerometerUpdatesToQueue(NSOperationQueue.mainQueue) { data, _ ->
            val acceleration = data?.acceleration ?: return@startAccelerometerUpdatesToQueue

            val (x, y, z) = acceleration.useContents {
                Triple(x.toFloat(), y.toFloat(), z.toFloat())
            }

            val currentRaw = sqrt((x * x + y * y + z * z).toDouble()).toFloat() * 9.81f
            smoothedValue = (currentRaw * alpha) + (smoothedValue * (1f - alpha))

            val gForce = smoothedValue / 9.81f
            _currentGForce.value = gForce
            _gForceHistory.update { history ->
                val newHistory = history.toMutableList()
                newHistory.add(gForce)
                if (newHistory.size > maxHistorySize) {
                    newHistory.removeAt(0)
                }
                newHistory
            }
        }
    }

    override fun stopTracking() {
        if (motionManager.accelerometerActive) {
            motionManager.stopAccelerometerUpdates()
        }
    }
}
