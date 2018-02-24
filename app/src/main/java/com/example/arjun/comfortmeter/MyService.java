package com.example.arjun.comfortmeter;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

public class MyService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastUpdate = 0;
    public static volatile boolean shouldContinue = true;


    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);


        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10000) {
                Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show();
                lastUpdate = curTime;
            }


        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}