package com.varunshankar.example36

import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.hardware.SensorManager
import android.view.View
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the recycler view
        mRecyclerView = findViewById<View>(R.id.rv_Master) as RecyclerView

        //Tell Android that we know the size of the recyclerview
        //doesn't change
        mRecyclerView!!.setHasFixedSize(true)

        //Set the layout manager
        layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager

        //Get the list of sensors
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        //Convert the list of sensors to string descriptions
        val inputList = getSensorStrings(sensors)

        //Set the adapter
        mAdapter = MyRVAdapter(inputList as MutableList<String>)
        mRecyclerView!!.adapter = mAdapter
    }

    fun getSensorStrings(sensors: List<Sensor?>): List<String> {
        val sensorStrings: MutableList<String> = ArrayList()
        var sensorData: String? = null
        var sensor: Sensor? = null
        for (i in sensors.indices) {
            sensor = sensors[i]
            sensorData = sensorTypeToString(sensor!!.type)
            sensorData = sensorData + " /Power:" + sensor.power
            sensorData = sensorData + " /Resolution:" + sensor.resolution
            sensorStrings.add(sensorData)
        }
        return sensorStrings
    }

    companion object {
        fun sensorTypeToString(sensorType: Int): String {
            return when (sensorType) {
                Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
                Sensor.TYPE_AMBIENT_TEMPERATURE -> "Ambient Temperature"
                Sensor.TYPE_GAME_ROTATION_VECTOR -> "Game Rotation Vector"
                Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> "Geomagnetic Rotation Vector"
                Sensor.TYPE_GRAVITY -> "Gravity"
                Sensor.TYPE_GYROSCOPE -> "Gyroscope"
                Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> "Gyroscope Uncalibrated"
                Sensor.TYPE_HEART_RATE -> "Heart Rate Monitor"
                Sensor.TYPE_LIGHT -> "Light"
                Sensor.TYPE_LINEAR_ACCELERATION -> "Linear Acceleration"
                Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
                Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> "Magnetic Field Uncalibrated"
                Sensor.TYPE_PRESSURE -> "Orientation"
                Sensor.TYPE_PROXIMITY -> "Proximity"
                Sensor.TYPE_RELATIVE_HUMIDITY -> "Relative Humidity"
                Sensor.TYPE_ROTATION_VECTOR -> "Rotation Vector"
                Sensor.TYPE_SIGNIFICANT_MOTION -> "Significant Motion"
                Sensor.TYPE_STEP_COUNTER -> "Step Counter"
                Sensor.TYPE_STEP_DETECTOR -> "Step Detector"
                else -> "Unknown"
            }
        }
    }
}