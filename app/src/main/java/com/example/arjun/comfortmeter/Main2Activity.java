package com.example.arjun.comfortmeter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class Main2Activity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float x,y,z;
    public static volatile LineGraphSeries<DataPoint> series;
    GraphView graph;
    private long lastUpdate = 0;
    private long startTime= 0;
    private TextView last_x, last_y, last_z, jerk;
    private double graph2LastXValue = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initalizeViews();

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {
        });
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);

    }


    public void startService(View view){
        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void stopService(View view){
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void saveData(){
        
    }

    public void initalizeViews(){
        last_x = (TextView) findViewById(R.id.last_x);
        last_y = (TextView) findViewById(R.id.last_y);
        last_z = (TextView) findViewById(R.id.last_z);
        jerk = (TextView) findViewById(R.id.jerk);
        lastUpdate = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 1000) {
                //x = sensorEvent.values[0];
                //series.appendData(new DataPoint(graph2LastXValue, x), true, 40);
                //graph2LastXValue += 1d;
                lastUpdate = curTime;
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = mRand.nextDouble()*0.15+0.3;
            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }
}