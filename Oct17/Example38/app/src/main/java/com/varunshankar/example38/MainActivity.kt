package com.varunshankar.example38

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private var mSensorManager: SensorManager? = null
    private var mGyroscope: Sensor? = null
    var mTvData: TextView? = null
    private val mThreshold = 0.2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get sensor manager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Get the default gyroscope sensor
        mGyroscope = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        //Get the text view
        mTvData = findViewById<View>(R.id.tv_data) as TextView
    }

    private val mListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {

            //Get the rotation rates along the x,y and z axes
            val xrot = sensorEvent.values[0].toDouble()
            val yrot = sensorEvent.values[1].toDouble()
            val zrot = sensorEvent.values[2].toDouble()

            //Compute the magnitude of the rotation
            val sq_magnitude = xrot.pow(2.0) + yrot.pow(2.0) + zrot.pow(2.0)

            //Check if this is greater than some threshold
            if (sq_magnitude > mThreshold) {
                //Change color of text view to some random color
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                mTvData!!.setBackgroundColor(color)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {
            Toast.makeText(applicationContext, "Accuracy Changed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mGyroscope != null) {
            mSensorManager!!.registerListener(
                mListener,
                mGyroscope,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (mGyroscope != null) {
            mSensorManager!!.unregisterListener(mListener)
        }
    }
}