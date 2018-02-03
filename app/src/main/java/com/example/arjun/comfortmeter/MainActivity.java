package com.example.arjun.comfortmeter;

        import android.content.Context;
        import android.content.Intent;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.TextView;
        import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float x,y,z;

    private long lastUpdate = 0;
    private TextView last_x, last_y, last_z, jerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set up the accelerometer
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalizeViews();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);


    }

    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    public void initalizeViews(){
        last_x = (TextView) findViewById(R.id.last_x);
        last_y = (TextView) findViewById(R.id.last_y);
        last_z = (TextView) findViewById(R.id.last_z);
        jerk = (TextView) findViewById(R.id.jerk);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {



            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 500) {
                long diffTime = (curTime - lastUpdate);

                float prevX = x;
                float prevY = y;
                float prevZ = z;

                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                double jerk1 = (java.lang.Math.sqrt(java.lang.Math.pow(x,2) + java.lang.Math.pow(y,2) + java.lang.Math.pow(z,2)) + java.lang.Math.sqrt(java.lang.Math.pow(prevX,2) + java.lang.Math.pow(prevY,2) + java.lang.Math.pow(prevZ,2)))/diffTime;

                lastUpdate = curTime;

                last_x.setText(Float.toString(x));
                last_y.setText(Float.toString(y));
                last_z.setText(Float.toString(z));
                jerk.setText(Double.toString(jerk1));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}



