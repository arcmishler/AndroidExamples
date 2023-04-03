package com.varunshankar.example25

import com.varunshankar.example25.NetworkUtils.buildURLFromString
import com.varunshankar.example25.NetworkUtils.getDataFromURL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val location = "Salt&Lake&City,us"
        val weatherDataURL = buildURLFromString(location)
        var jsonWeatherData: String? = null
        try {
            assert(weatherDataURL != null)
            jsonWeatherData = getDataFromURL(weatherDataURL!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}