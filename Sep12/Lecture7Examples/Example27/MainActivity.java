package com.varunshankar.example27;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity{
    private Fragment weatherFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null)
        {
            weatherFragment = getSupportFragmentManager().findFragmentByTag("weather_frag");
        }
        else {
            WeatherFragment weatherFragment = new WeatherFragment();
            FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fl_frag_weather, weatherFragment, "weather_frag");
            fTrans.commit();
        }
    }
}





