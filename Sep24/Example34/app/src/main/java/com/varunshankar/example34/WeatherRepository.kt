package com.varunshankar.example34

import com.varunshankar.example34.NetworkUtils.buildURLFromString
import com.varunshankar.example34.NetworkUtils.getDataFromURL
import com.varunshankar.example34.JSONWeatherUtils.getWeatherData
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.core.os.HandlerCompat
import android.os.Looper
import org.json.JSONException
import java.lang.Exception
import java.util.concurrent.Executors
import kotlin.jvm.Synchronized

class WeatherRepository private constructor(application: Application) {
    val data = MutableLiveData<WeatherData>()
    private var mLocation: String? = null
    fun setLocation(location: String?) {
        mLocation = location
        loadData()
    }

    private fun loadData() {
        FetchWeatherTask().execute(mLocation)
    }

    private inner class FetchWeatherTask {
        var executorService = Executors.newSingleThreadExecutor()
        var mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun execute(location: String?) {
            executorService.execute {
                var jsonWeatherData: String?
                val weatherDataURL = buildURLFromString(location!!)
                jsonWeatherData = null
                try {
                    jsonWeatherData = getDataFromURL(weatherDataURL!!)
                    jsonWeatherData?.let { postToMainThread(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(retrievedJsonData: String) {
            mainThreadHandler.post {
                try {
                    data.setValue(getWeatherData(retrievedJsonData))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: WeatherRepository? = null
        @Synchronized
        fun getInstance(application: Application): WeatherRepository {
            if (instance == null) {
                instance = WeatherRepository(application)
            }
            return instance as WeatherRepository
        }
    }

    init {
        if (mLocation != null) loadData()
    }
}