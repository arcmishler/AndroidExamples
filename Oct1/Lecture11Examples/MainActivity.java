package com.example.example35;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{
    private EditText mEtLocation;
    private TextView mTvTemp;
    private TextView mTvPress;
    private TextView mTvHum;
    private Button mBtSubmit;
    private String mLocation;
    private WeatherViewModel mWeatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the edit text and all the text views
        mEtLocation = (EditText) findViewById(R.id.et_location);
        mTvTemp = (TextView) findViewById(R.id.tv_temp);
        mTvPress = (TextView) findViewById(R.id.tv_pressure);
        mTvHum = (TextView) findViewById(R.id.tv_humidity);

        mBtSubmit = (Button) findViewById(R.id.button_submit);
        mBtSubmit.setOnClickListener(this);

        //Create the view model
        mWeatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        //Set the observer
        (mWeatherViewModel.getData()).observe(this,nameObserver);

    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<WeatherData> nameObserver  = new Observer<WeatherData>() {
        @Override
        public void onChanged(@Nullable final WeatherData weatherData) {
            // Update the UI if this data variable changes
            if(weatherData!=null) {
                mTvTemp.setText("" + Math.round(weatherData.getTemperature().getTemp() - 273.15) + " C");
                mTvHum.setText("" + weatherData.getCurrentCondition().getHumidity() + "%");
                mTvPress.setText("" + weatherData.getCurrentCondition().getPressure() + " hPa");
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_submit:{
                //Get the string from the edit text and sanitize the input
                String inputFromEt = mEtLocation.getText().toString().replace(' ','&');
                loadWeatherData(inputFromEt);
            }
            break;
        }
    }

    void loadWeatherData(String location){
        //pass the location in to the view model
        mWeatherViewModel.setLocation(location);
    }
}

