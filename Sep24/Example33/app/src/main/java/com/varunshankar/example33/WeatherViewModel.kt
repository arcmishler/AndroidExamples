package com.varunshankar.example33

import com.varunshankar.example33.NetworkUtils.buildURLFromString
import com.varunshankar.example33.NetworkUtils.getDataFromURL
import com.varunshankar.example33.JSONWeatherUtils.getWeatherData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.core.os.HandlerCompat
import android.os.Looper
import org.json.JSONException
import java.lang.Exception
import java.util.concurrent.Executors

class WeatherViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val jsonData = MutableLiveData<WeatherData>()
    private var mLocation: String? = null
    fun setLocation(location: String?) {
        mLocation = location
        loadData()
    }

    val data: LiveData<WeatherData>
        get() = jsonData

    private fun loadData() {
        if (mLocation!=null)
            LoadTask().execute(mLocation)
    }

    private inner class LoadTask {
        val executorService = Executors.newSingleThreadExecutor()!!
        val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        fun execute(location: String?) {
            executorService.execute {
                var jsonWeatherData: String?
                val weatherDataURL = buildURLFromString(location!!)
                jsonWeatherData = null
                try {
                    jsonWeatherData = getDataFromURL(weatherDataURL!!)
                    jsonWeatherData?.let { postToMainThread(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun postToMainThread(retrievedJsonData: String) {
            mainThreadHandler.post {
                try {
                    jsonData.setValue(getWeatherData(retrievedJsonData))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    } /*   private static class LoadTask extends AsyncTask<String,Void,String>{
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