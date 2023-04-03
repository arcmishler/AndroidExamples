package com.varunshankar.example25;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Declare methods as static. We don't want to create objects of this class.
public class JSONWeatherUtils {
    public static WeatherData getWeatherData(String data) throws JSONException{
        WeatherData weatherData = new WeatherData();
        LocationData locationData = new LocationData();

        //Start parsing JSON data
        JSONObject jsonObject = new JSONObject(data); //Must throw JSONException

        //Get location data
        JSONObject jsonCoords = jsonObject.getJSONObject("coord");
        locationData.setLatitude(jsonCoords.getDouble("lat"));
        locationData.setLongitude(jsonCoords.getDouble("long"));
        JSONObject jsonSys = jsonObject.getJSONObject("sys");
        locationData.setCountry(jsonSys.getString("country"));
        locationData.setCity(jsonObject.getString("name"));
        locationData.setSunrise(jsonSys.getLong("sunrise"));
        locationData.setSunset(jsonSys.getLong("sunrise"));

        //Pass location data to weather class
        weatherData.setLocationData(locationData);

        //Get the actual weather information
        JSONArray jsonWeatherArray = jsonObject.getJSONArray("weather");
        JSONObject jsonWeather = jsonWeatherArray.getJSONObject(0); //use only the first element of weather array

        //Get the current condition
        WeatherData.CurrentCondition currentCondition = weatherData.getCurrentCondition();
        currentCondition.setWeatherId(jsonWeather.getLong("id"));
        currentCondition.setDescr(jsonWeather.getString("description"));
        currentCondition.setCondition(jsonWeather.getString("main"));
        currentCondition.setIcon(jsonWeather.getString("icon"));

        JSONObject jsonMain = jsonObject.getJSONObject("main");
        currentCondition.setHumidity(jsonMain.getLong("humidity"));
        currentCondition.setPressure(jsonMain.getLong("pressure"));
        weatherData.setCurrentCondition(currentCondition);

        //Get the temperature, wind and cloud data.
        WeatherData.Temperature temperature = weatherData.getTemperature();
        WeatherData.Wind wind = weatherData.getWind();
        WeatherData.Clouds clouds = weatherData.getClouds();
        temperature.setMaxTemp(jsonWeather.getDouble("temp_max"));
        temperature.setMinTemp(jsonWeather.getDouble("temp_min"));
        temperature.setTemp(jsonWeather.getDouble("temp"));
        weatherData.setTemperature(temperature);

        JSONObject jsonWind = jsonObject.getJSONObject("wind");
        JSONObject jsonClouds = jsonObject.getJSONObject("clouds");
        wind.setSpeed(jsonWind.getDouble("speed"));
        wind.setDeg(jsonWind.getDouble("deg"));
        weatherData.setWind(wind);
        clouds.setPerc(jsonClouds.getLong("all"));
        weatherData.setClouds(clouds);
        return weatherData;
    }
}
