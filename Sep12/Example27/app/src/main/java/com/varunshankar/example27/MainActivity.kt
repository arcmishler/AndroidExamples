package com.varunshankar.example27

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    private var weatherFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            weatherFragment = supportFragmentManager.findFragmentByTag("weather_frag")
        } else {
            weatherFragment = WeatherFragment()
            val fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fl_frag_weather, weatherFragment as WeatherFragment, "weather_frag")
            fTrans.commit()
        }
    }
}