package com.example.example35

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized

class WeatherRepository private constructor(weatherDao: WeatherDao) {
    // This LiveData object that is notified when we've fetched the weather
    val data = MutableLiveData<WeatherData>()

    // This flow is triggered when any change happens to the database
    val allCityWeather: Flow<List<WeatherTable>> = weatherDao.getAllWeather()

    private var mLocation: String? = null
    private var mJsonString: String? = null
    private var mWeatherDao: WeatherDao = weatherDao

    fun setLocation(location: String) {
        // First cache the location
        mLocation = location

        // Everything within the scope happens logically sequentially
        mScope.launch(Dispatchers.IO){
            //fetch data on a worker thread
            fetchAndParseWeatherData(location)

            // After the suspend function returns, Update the View THEN insert into db
            if(mJsonString!=null) {
                // Populate live data object. But since this is happening in a background thread (the coroutine),
                // we have to use postValue rather than setValue. Use setValue if update is on main thread
                data.postValue( JSONWeatherUtils.getWeatherData(mJsonString))

                // insert into db. This will trigger a flow
                // that updates a recyclerview. All db ops should happen
                // on a background thread
                insert()
            }
        }
    }

    @WorkerThread
    suspend fun insert() {
        if (mLocation != null && mJsonString!=null) {
            mWeatherDao.insert(WeatherTable(mLocation!!,mJsonString!!))
        }
    }

    @WorkerThread
    suspend fun fetchAndParseWeatherData(location: String) {
        val weatherDataURL = NetworkUtils.buildURLFromString(location)
        if(weatherDataURL!=null) {
            // This is actually a blocking call unless you're using an
            // asynchronous IO library (which we're not). However, it is a blocking
            // call on a background thread, not on the UI thread
            val jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL)
            if (jsonWeatherData != null) {
                mJsonString = jsonWeatherData
            }
        }
    }

    // Make the repository singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        private var mInstance: WeatherRepository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(weatherDao: WeatherDao,
        scope: CoroutineScope
        ): WeatherRepository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = WeatherRepository(weatherDao)
                mInstance = instance
                instance
            }
        }
    }
}