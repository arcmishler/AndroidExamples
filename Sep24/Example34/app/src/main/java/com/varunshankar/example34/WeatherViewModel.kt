package com.varunshankar.example34

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.varunshankar.example34.WeatherData
import com.varunshankar.example34.WeatherRepository
import androidx.lifecycle.LiveData

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val jsonData: MutableLiveData<WeatherData>
    private val mWeatherRepository: WeatherRepository
    fun setLocation(location: String?) {
        mWeatherRepository.setLocation(location)
    }

    val data: LiveData<WeatherData>
        get() = jsonData

    init {
        mWeatherRepository = WeatherRepository.getInstance(application)
        jsonData = mWeatherRepository.data
    }
}