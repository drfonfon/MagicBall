package com.fonfon.magicball

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.lang.Math.*

class ShakeDetector(
    context: Context?, //context являяется локальной переменной и используется только в конструкторе
    private var threshold: Float = 10f, //treshold - сила тряски с предустановленным значением
    val shakeCall: () -> Unit //shakeCall - lambda функция
) : SensorEventListener {

    //Можно "выносить" объекты, объявляемые в конструкторе из конструктора
    private val sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager?

    private var accelerate = 0f //общее значение ускорение
    private var accelerateCurrent = SensorManager.GRAVITY_EARTH //текущее ускорение в данный момент

    fun start() = sensorManager?.registerListener(
        this,
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), // выыбираем акселерометр
        SensorManager.SENSOR_DELAY_FASTEST //максимальная скорость сенсора
    )

    fun stop() = sensorManager?.unregisterListener(this)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val last = accelerateCurrent
            accelerateCurrent = sqrt(
                pow(it.values[0].toDouble(), 2.0)
                        + pow(it.values[1].toDouble(), 2.0)
                        + pow(it.values[2].toDouble(), 2.0)
            ).toFloat() //текущее ускорение - длинна вектора ускорения
            val delta = accelerateCurrent - last //вычисляем текующую длинну сфвига
            accelerate = accelerate * 0.9f + delta
            if (accelerate > threshold) {
                shakeCall()
            }
        }
    }


}