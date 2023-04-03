package com.varunshankar.example39

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    var mTvData: TextView? = null
    private val mThreshold = 2.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get sensor manager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //get the sensor
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //Get the text view
        mTvData = findViewById<View>(R.id.tv_data) as TextView
    }

    private val mListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {

            //Get the acceleration rates along the x and z axes
            val x = sensorEvent.values[0].toDouble()
            val z = sensorEvent.values[2].toDouble()

            //Check if this is greater than some threshold
            if (abs(x) > mThreshold || abs(z) > mThreshold) {
                //Change color of text view to some random color
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                mTvData!!.setBackgroundColor(color)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
    }

    override fun onResume() {
        super.onResume()
        if (mAccelerometer != null) {
            mSensorManager!!.registerListener(
                mListener,
                mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (mAccelerometer != null) {
            mSensorManager!!.unregisterListener(mListener)
        }
    }
}