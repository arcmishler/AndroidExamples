package com.varunshankar.example37;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mLight;
    public TextView mTvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Get the default light sensor
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //Get the text view
        mTvData = (TextView) findViewById(R.id.tv_data);

    }

    private SensorEventListener mLightListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            //Get the illumination value
            float lightVal = sensorEvent.values[0];

            //Update the text view
            mTvData.setText(""+ String.valueOf(lightVal));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            Toast.makeText(getApplicationContext(),"Accuracy changed!",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(mLight!=null){
            mSensorManager.registerListener(mLightListener,mLight,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLight!=null){
            mSensorManager.unregisterListener(mLightListener);
        }
    }


}
