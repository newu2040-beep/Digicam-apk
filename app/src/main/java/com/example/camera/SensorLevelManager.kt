package com.example.camera

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.atan2

class SensorLevelManager(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _pitch = MutableStateFlow(0f)
    val pitch: StateFlow<Float> = _pitch

    private val _roll = MutableStateFlow(0f)
    val roll: StateFlow<Float> = _roll

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val calculatedRoll = atan2(x.toDouble(), y.toDouble()) * (180 / Math.PI)
        val calculatedPitch = atan2(y.toDouble(), z.toDouble()) * (180 / Math.PI)

        _roll.value = calculatedRoll.toFloat()
        _pitch.value = calculatedPitch.toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
