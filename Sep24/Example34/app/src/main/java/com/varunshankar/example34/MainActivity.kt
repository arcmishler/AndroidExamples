package com.varunshankar.example34

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mEtLocation: EditText? = null
    private var mTvTemp: TextView? = null
    private var mTvPress: TextView? = null
    private var mTvHum: TextView? = null
    private var mBtSubmit: Button? = null
    private val mLocation: String? = null
    private var mWeatherViewModel: WeatherViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the edit text and all the text views
        mEtLocation = findViewById<View>(R.id.et_location) as EditText
        mTvTemp = findViewById<View>(R.id.tv_temp) as TextView
        mTvPress = findViewById<View>(R.id.tv_pressure) as TextView
        mTvHum = findViewById<View>(R.id.tv_humidity) as TextView
        mBtSubmit = findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)

        //Create the view model
        mWeatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        //Set the observer
        mWeatherViewModel!!.data.observe(this, nameObserver)
    }

    //create an observer that watches the LiveData<WeatherData> object
    private val nameObserver: Observer<WeatherData> =
        Observer { weatherData -> // Update the UI if this data variable changes
            if (weatherData != null) {
                mTvTemp!!.text = "" + (weatherData.temperature.temp - 273.15).roundToInt() + " C"
                mTvHum!!.text = "" + weatherData.currentCondition.humidity + "%"
                mTvPress!!.text = "" + weatherData.currentCondition.pressure + " hPa"
            }
        }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //Get the string from the edit text and sanitize the input
                val inputFromEt = mEtLocation!!.text.toString().replace(' ', '&')
                loadWeatherData(inputFromEt)
            }
        }
    }

    private fun loadWeatherData(location: String?) {
        //pass the location in to the view model
        mWeatherViewModel!!.setLocation(location)
    }
}