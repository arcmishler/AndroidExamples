package com.varunshankar.example27;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WeatherFragment extends Fragment implements View.OnClickListener{

    private EditText mEtLocation;
    private TextView mTvTemp;
    private TextView mTvPress;
    private TextView mTvHum;
    private WeatherData mWeatherData;
    private Button mBtSubmit;
    private static FetchWeatherTask mFetchWeatherTask = new FetchWeatherTask();

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        //Get the edit text and all the text views
        mEtLocation = (EditText) view.findViewById(R.id.et_location);
        mTvTemp = (TextView) view.findViewById(R.id.tv_temp);
        mTvPress = (TextView) view.findViewById(R.id.tv_pressure);
        mTvHum = (TextView) view.findViewById(R.id.tv_humidity);
        if(savedInstanceState!=null) {
            String temp = savedInstanceState.getString("tvTemp");
            String hum = savedInstanceState.getString("tvHum");
            String press = savedInstanceState.getString("tvPress");
            if (temp != null)
                mTvTemp.setText(""+temp);
            if (hum != null)
                mTvHum.setText(""+hum);
            if (press != null)
                mTvPress.setText(""+press);
        }
        mFetchWeatherTask.setWeakReference(this); //make sure we're always pointing to current version of fragment
        mBtSubmit = (Button) view.findViewById(R.id.button_submit);
        mBtSubmit.setOnClickListener(this);
        return view;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tvTemp",mTvTemp.getText().toString());
        outState.putString("tvHum",mTvHum.getText().toString());
        outState.putString("tvPress",mTvPress.getText().toString());
    }

    private void loadWeatherData(String location){
      mFetchWeatherTask.execute(this,location);
    }
    private static class FetchWeatherTask{
        WeakReference<WeatherFragment> weatherFragmentWeakReference;
        private ExecutorService executorService = Executors.newSingleThreadExecutor();
        private Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

        public void setWeakReference(WeatherFragment ref)
        {
            weatherFragmentWeakReference = new WeakReference<WeatherFragment>(ref);
        }
        public void execute(WeatherFragment ref, String location){

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
            WeatherFragment localRef = weatherFragmentWeakReference.get();
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (jsonWeatherData != null) {
                        try {
                            localRef.mWeatherData = JSONWeatherUtils.getWeatherData(jsonWeatherData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (localRef.mWeatherData != null) {
                            localRef.mTvTemp.setText("" + Math.round(localRef.mWeatherData.getTemperature().getTemp() - 273.15) + " C");
                            localRef.mTvHum.setText("" + localRef.mWeatherData.getCurrentCondition().getHumidity() + "%");
                            localRef.mTvPress.setText("" + localRef.mWeatherData.getCurrentCondition().getPressure() + " hPa");
                        }
                    }
                }
            });
        }
    }
}