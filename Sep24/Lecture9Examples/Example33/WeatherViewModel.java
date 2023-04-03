package com.varunshankar.example33;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherViewModel extends AndroidViewModel {
    private MutableLiveData<WeatherData> jsonData =
            new MutableLiveData<WeatherData>();
    private String mLocation;

    public WeatherViewModel(Application application){
        super(application);
    }

    public void setLocation(String location){
        mLocation = location;
        loadData();
    }

    public LiveData<WeatherData> getData(){
        return jsonData;
    }

     private void loadData(){
        new LoadTask().execute(mLocation);
    }
    private class LoadTask{
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
                        if(jsonWeatherData!=null)
                            postToMainThread(jsonWeatherData);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        private void postToMainThread(String retrievedJsonData)
        {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsonData.setValue(JSONWeatherUtils.getWeatherData(retrievedJsonData));
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

 /*   private static class LoadTask extends AsyncTask<String,Void,String>{
        private WeakReference<WeatherViewModel> mWeatherViewModelWeakReference;

        LoadTask(WeatherViewModel weatherViewModel)
        {
            mWeatherViewModelWeakReference = new WeakReference<WeatherViewModel>(weatherViewModel);
        }

        @Override
        protected String doInBackground(String... strings) {
            String location = strings[0];
            URL weatherDataURL = null;
            String retrievedJsonData = null;
            if(location!=null) {
                weatherDataURL = NetworkUtils.buildURLFromString(location);
                try {
                    retrievedJsonData = NetworkUtils.getDataFromURL(weatherDataURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return retrievedJsonData;
        }

        @Override
        protected void onPostExecute(String retrievedJsonData) {
            try {
                if (mWeatherViewModelWeakReference==null) return;
                WeatherViewModel local = mWeatherViewModelWeakReference.get();
                local.jsonData.setValue(JSONWeatherUtils.getWeatherData(retrievedJsonData));
            }catch(JSONException e){
                e.printStackTrace();
            }

        }
    };*/
}
