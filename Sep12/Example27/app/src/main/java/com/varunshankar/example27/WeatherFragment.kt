package com.varunshankar.example27

import com.varunshankar.example27.NetworkUtils.buildURLFromString
import com.varunshankar.example27.NetworkUtils.getDataFromURL
import android.widget.EditText
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.core.os.HandlerCompat
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import org.json.JSONException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

class WeatherFragment : Fragment(), View.OnClickListener {
    private var mEtLocation: EditText? = null
    private var mTvTemp: TextView? = null
    private var mTvPress: TextView? = null
    private var mTvHum: TextView? = null
    private var mWeatherData: WeatherData? = null
    private var mBtSubmit: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        //Get the edit text and all the text views
        mEtLocation = view.findViewById<View>(R.id.et_location) as EditText
        mTvTemp = view.findViewById<View>(R.id.tv_temp) as TextView
        mTvPress = view.findViewById<View>(R.id.tv_pressure) as TextView
        mTvHum = view.findViewById<View>(R.id.tv_humidity) as TextView
        if (savedInstanceState != null) {
            val temp = savedInstanceState.getString("tvTemp")
            val hum = savedInstanceState.getString("tvHum")
            val press = savedInstanceState.getString("tvPress")
            if (temp != null) mTvTemp!!.text = "" + temp
            if (hum != null) mTvHum!!.text = "" + hum
            if (press != null) mTvPress!!.text = "" + press
        }
        mFetchWeatherTask.setWeakReference(this) //make sure we're always pointing to current version of fragment
        mBtSubmit = view.findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)
        return view
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tvTemp", mTvTemp!!.text.toString())
        outState.putString("tvHum", mTvHum!!.text.toString())
        outState.putString("tvPress", mTvPress!!.text.toString())
    }

    private fun loadWeatherData(location: String) {
        mFetchWeatherTask.execute(location)
    }

    private class FetchWeatherTask {
        var weatherFragmentWeakReference: WeakReference<WeatherFragment>? = null
        private val executorService = Executors.newSingleThreadExecutor()
        private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun setWeakReference(ref: WeatherFragment) {
            weatherFragmentWeakReference = WeakReference(ref)
        }

        fun execute(location: String?) {
            executorService.execute {
                var jsonWeatherData: String?
                val weatherDataURL = buildURLFromString(location!!)
                jsonWeatherData = null
                try {
                    jsonWeatherData = getDataFromURL(weatherDataURL!!)
                    postToMainThread(jsonWeatherData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(jsonWeatherData: String?) {
            val localRef = weatherFragmentWeakReference!!.get()
            mainThreadHandler.post {
                if (jsonWeatherData != null) {
                    try {
                        localRef!!.mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (localRef!!.mWeatherData != null) {
                        localRef.mTvTemp!!.text =
                            "" + Math.round(localRef.mWeatherData!!.temperature.temp - 273.15) + " C"
                        localRef.mTvHum!!.text =
                            "" + localRef.mWeatherData!!.currentCondition.humidity + "%"
                        localRef.mTvPress!!.text =
                            "" + localRef.mWeatherData!!.currentCondition.pressure + " hPa"
                    }
                }
            }
        }
    }

    companion object {
        private val mFetchWeatherTask = FetchWeatherTask()
    }
}