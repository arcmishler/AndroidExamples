package com.varunshankar.example37

import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.hardware.SensorManager
import android.widget.TextView
import android.os.Bundle
import android.hardware.SensorEventListener
import android.hardware.SensorEvent
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var mSensorManager: SensorManager? = null
    private var mLight: Sensor? = null
    var mTvData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get sensor manager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Get the default light sensor
        mLight = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        //Get the text view
        mTvData = findViewById<View>(R.id.tv_data) as TextView
    }

    private val mLightListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {

            //Get the illumination value
            val lightVal = sensorEvent.values[0]

            //Update the text view
            mTvData!!.text = "" + lightVal.toString()
        }

        override fun onAccuracyChanged(sensor: Sensor, i: Int) {
            Toast.makeText(applicationContext, "Accuracy changed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mLight != null) {
            mSensorManager!!.registerListener(
                mLightListener,
                mLight,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (mLight != null) {
            mSensorManager!!.unregisterListener(mLightListener)
        }
    }
}