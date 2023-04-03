package com.varunshankar.example25;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String location = "Salt&Lake&City,us";
        URL weatherDataURL = NetworkUtils.buildURLFromString(location);
        String jsonWeatherData = null;
        try{
            jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
