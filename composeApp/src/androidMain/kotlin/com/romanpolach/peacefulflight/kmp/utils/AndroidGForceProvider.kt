package com.romanpolach.peacefulflight.kmp.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.sqrt

class AndroidGForceProvider(context: Context) : GForceProvider, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _currentGForce = MutableStateFlow(1.0f)
    override val currentGForce: StateFlow<Float> = _currentGForce.asStateFlow()

    private val _gForceHistory = MutableStateFlow<List<Float>>(emptyList())
    override val gForceHistory: StateFlow<List<Float>> = _gForceHistory.asStateFlow()

    private val _isSensorAvailable = MutableStateFlow(accelerometer != null)
    override val isSensorAvailable: StateFlow<Boolean> = _isSensorAvailable.asStateFlow()

    private var smoothedValue = 9.81f
    private val alpha = 0.05f
    private val maxHistorySize = 300

    override fun startTracking() {
        accelerometer?.let {
            _isSensorAvailable.value = true
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        } ?: run {
            _isSensorAvailable.value = false
        }
    }

    override fun stopTracking() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            // Calculate magnitude
            val currentRaw = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            // Apply Low-Pass Filter
            smoothedValue = (currentRaw * alpha) + (smoothedValue * (1f - alpha))

            // Convert to G-Force
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
