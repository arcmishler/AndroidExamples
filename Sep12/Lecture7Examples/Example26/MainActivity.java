package com.varunshankar.example26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity
                        implements View.OnClickListener{

    private EditText mEtLocation;
    private TextView mTvTemp;
    private TextView mTvPress;
    private TextView mTvHum;
    private WeatherData mWeatherData;
    private Button mBtSubmit;

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
    }
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

    private void loadWeatherData(String location){
        new FetchWeatherTask().execute(location);
    }

    private class FetchWeatherTask{
       ExecutorService executorService = Executors.newSingleThreadExecutor();
       Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
       public void execute(String location)
       {
            executorService.execute(new Runnable(){
                @Override
                public void run(){
                    String jsonWeatherData;
                    URL weatherDataURL = NetworkUtils.buildURLFromString(location);
                    jsonWeatherData = null;
                    try{
                        jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL);
                        postToMainThread(jsonWeatherData);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
       }

       private void postToMainThread(String jsonWeatherData)
       {
           mainThreadHandler.post(new Runnable() {
               @Override
               public void run() {
                   if (jsonWeatherData != null) {
                       try {
                           mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       if (mWeatherData != null) {
                           mTvTemp.setText("" + Math.round(mWeatherData.getTemperature().getTemp() - 273.15) + " C");
                           mTvHum.setText("" + mWeatherData.getCurrentCondition().getHumidity() + "%");
                           mTvPress.setText("" + mWeatherData.getCurrentCondition().getPressure() + " hPa");
                       }
                   }
               }
           });
       }
    }

/*    private class FetchWeatherTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... inputStringArray) {
            String location = inputStringArray[0];
            URL weatherDataURL = NetworkUtils.buildURLFromString(location);
            String jsonWeatherData = null;
            try{
                jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL);
                return jsonWeatherData;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonWeatherData) {
            if (jsonWeatherData!=null){
                try {
                    mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                if(mWeatherData!=null) {
                    mTvTemp.setText("" + Math.round(mWeatherData.getTemperature().getTemp() - 273.15) + " C");
                    mTvHum.setText("" + mWeatherData.getCurrentCondition().getHumidity() + "%");
                    mTvPress.setText("" + mWeatherData.getCurrentCondition().getPressure() + " hPa");
                }
            }
        }
    }*/
}
