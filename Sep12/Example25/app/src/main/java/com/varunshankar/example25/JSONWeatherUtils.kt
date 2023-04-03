package com.varunshankar.example25

import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject


//Declare methods as static. We don't want to create objects of this class.
object JSONWeatherUtils {
    @Throws(JSONException::class)
    fun getWeatherData(data: String?): WeatherData {
        val weatherData = WeatherData()
        val locationData = LocationData()

        //Start parsing JSON data
        val jsonObject = JSONObject(data!!) //Must throw JSONException

        //Get location data
        val jsonCoords = jsonObject.getJSONObject("coord")
        locationData.latitude = jsonCoords.getDouble("lat")
        locationData.longitude = jsonCoords.getDouble("long")
        val jsonSys = jsonObject.getJSONObject("sys")
        locationData.country = jsonSys.getString("country")
        locationData.city = jsonObject.getString("name")
        locationData.sunrise = jsonSys.getLong("sunrise")
        locationData.sunset = jsonSys.getLong("sunrise")

        //Pass location data to weather class
        weatherData.locationData = locationData

        //Get the actual weather information
        val jsonWeatherArray = jsonObject.getJSONArray("weather")
        val jsonWeather =
            jsonWeatherArray.getJSONObject(0) //use only the first element of weather array

        //Get the current condition
        val currentCondition = weatherData.currentCondition
        currentCondition!!.weatherId = jsonWeather.getLong("id")
        currentCondition.descr = jsonWeather.getString("description")
        currentCondition.condition = jsonWeather.getString("main")
        currentCondition.icon = jsonWeather.getString("icon")
        val jsonMain = jsonObject.getJSONObject("main")
        currentCondition.humidity = jsonMain.getLong("humidity").toDouble()
        currentCondition.pressure = jsonMain.getLong("pressure").toDouble()
        weatherData.currentCondition = currentCondition

        //Get the temperature, wind and cloud data.
        val temperature = weatherData.temperature
        val wind = weatherData.wind
        val clouds = weatherData.clouds
        temperature!!.maxTemp = jsonWeather.getDouble("temp_max")
        temperature.minTemp = jsonWeather.getDouble("temp_min")
        temperature.temp = jsonWeather.getDouble("temp")
        weatherData.temperature = temperature
        val jsonWind = jsonObject.getJSONObject("wind")
        val jsonClouds = jsonObject.getJSONObject("clouds")
        wind!!.speed = jsonWind.getDouble("speed")
        wind.deg = jsonWind.getDouble("deg")
        weatherData.wind = wind
        clouds!!.perc = jsonClouds.getLong("all")
        weatherData.clouds = clouds
        return weatherData
    }
}