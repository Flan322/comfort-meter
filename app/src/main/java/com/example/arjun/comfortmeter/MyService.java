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
    private double graph2LastXValue = 0d;
    private float x;

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
        Main2Activity.series.resetData(new DataPoint[] {});

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

            if ((curTime - lastUpdate) > 1000) {
                x = sensorEvent.values[0];
                DataPoint data = new DataPoint(graph2LastXValue, x);
                Main2Activity.series.appendData(data, true, 40);

                //Here is where we add the data to the database for use later
                //DatabaseHandler database = new DatabaseHandler(this);
                //database.addData(data);


                double minX, minY, maxX, maxY;

                Main2Activity.graph.getViewport().calcCompleteRange();
                maxX = Main2Activity.graph.getViewport().getMaxX(true);
                maxY = Main2Activity.graph.getViewport().getMaxY(true);
                minX = Main2Activity.graph.getViewport().getMinX(true);
                minY = Main2Activity.graph.getViewport().getMinY(true);

                Main2Activity.graph.getViewport().setMaxX(maxX);
                Main2Activity.graph.getViewport().setMaxY(maxY);
                Main2Activity.graph.getViewport().setMinX(minX);
                Main2Activity.graph.getViewport().setMinY(minY);

                graph2LastXValue += 1d;
                lastUpdate = curTime;

            }


        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}