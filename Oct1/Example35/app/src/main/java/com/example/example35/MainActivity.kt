package com.example.example35

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mEtLocation: EditText? = null
    private var mTvTemp: TextView? = null
    private var mTvPress: TextView? = null
    private var mTvHum: TextView? = null
    private var mBtSubmit: Button? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null

    // Initialize the view model here. One per activity.
    // While initializing, we'll also inject the repository.
    // However, standard view model constructor only takes a context to
    // the activity. We'll need to define our own constructor, but this
    // requires writing our own view model factory.
    private val mWeatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((application as WeatherApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get the edit text, all the text views, button
        mEtLocation = findViewById<View>(R.id.et_location) as EditText
        mTvTemp = findViewById<View>(R.id.tv_temp) as TextView
        mTvPress = findViewById<View>(R.id.tv_pressure) as TextView
        mTvHum = findViewById<View>(R.id.tv_humidity) as TextView
        mBtSubmit = findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)

        //Set the observer for the vanilla livedata object
        mWeatherViewModel.data.observe(this, liveDataObserver)

        //set another observer for the flow-converted-to-livedata object
        // cannot attach observers directly to flows, but the flow
        // has already been converted to a livedata object
        mWeatherViewModel.allCityWeather.observe(this, flowObserver)

        //Get the recycler view.
        mRecyclerView = findViewById<View>(R.id.rv_Master) as RecyclerView

        //Tell Android that we know the size of the recyclerview
        //doesn't change
        mRecyclerView!!.setHasFixedSize(true)

        //Set the layout manager
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = layoutManager
    }

    //create an observer that watches the LiveData<WeatherData> object
    private val liveDataObserver: Observer<WeatherData> =
        Observer { weatherData -> // Update the UI if this data variable changes
            if (weatherData != null) {
                mTvTemp!!.text = "" + (weatherData.temperature.temp - 273.15).roundToInt() + " C"
                mTvHum!!.text = "" + weatherData.currentCondition.humidity + "%"
                mTvPress!!.text = "" + weatherData.currentCondition.pressure + " hPa"
            }
        }

    // This observer is triggered when the Flow object in the repository
    // detects a change to the database (including at the start of the app)
    private val flowObserver: Observer<List<WeatherTable>> =
        Observer { weatherTableList ->
            if (weatherTableList != null) {
                // Pass the entire list to a RecyclerView
                mRecyclerView!!.adapter = WeatherRVAdapter(weatherTableList)
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
        mWeatherViewModel.setLocation(location!!)
    }
}