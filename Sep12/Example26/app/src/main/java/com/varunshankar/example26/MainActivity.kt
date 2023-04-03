package com.varunshankar.example26

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import androidx.core.os.HandlerCompat
import android.os.Looper
import android.view.View
import android.widget.Button
import org.json.JSONException
import java.lang.Exception
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mEtLocation: EditText? = null
    private var mTvTemp: TextView? = null
    private var mTvPress: TextView? = null
    private var mTvHum: TextView? = null
    private var mWeatherData: WeatherData? = null
    private var mBtSubmit: Button? = null
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

    private fun loadWeatherData(location: String) {
        FetchWeatherTask().execute(location)
    }

    private inner class FetchWeatherTask {
        var executorService = Executors.newSingleThreadExecutor()!!
        var mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun execute(location: String?) {
            executorService.execute {
                var jsonWeatherData: String?
                val weatherDataURL = NetworkUtils.buildURLFromString(location!!)
                try {
                    jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL!!)
                    postToMainThread(jsonWeatherData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(jsonWeatherData: String?) {
            mainThreadHandler.post {
                if (jsonWeatherData != null) {
                    try {
                        mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (mWeatherData != null) {
                        mTvTemp!!.text =
                            "" + (mWeatherData!!.temperature.temp - 273.15).roundToInt() + " C"
                        mTvHum!!.text = "" + mWeatherData!!.currentCondition.humidity + "%"
                        mTvPress!!.text = "" + mWeatherData!!.currentCondition.pressure + " hPa"
                    }
                }
            }
        }
    }
// Use this to discuss AsyncTask, mention its deprecation. Don't talk about leaks just yet.
/*private class FetchWeatherTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... inputStringArray) {
            String location = inputStringArray[0];
            URL weatherDataURL = NetworkUtils.buildURLFromString(location);
            String jsonWeatherData = null;
            try{
                jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL);
                return jsonWeatherData;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonWeatherData) {
            if (jsonWeatherData!=null){
                try {
                    mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                if(mWeatherData!=null) {
                    mTvTemp.setText("" + Math.round(mWeatherData.getTemperature().getTemp() - 273.15) + " C");
                    mTvHum.setText("" + mWeatherData.getCurrentCondition().getHumidity() + "%");
                    mTvPress.setText("" + mWeatherData.getCurrentCondition().getPressure() + " hPa");
                }
            }
        }
    }*/
}