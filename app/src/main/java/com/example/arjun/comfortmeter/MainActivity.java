package com.example.arjun.comfortmeter;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.preference.Preference;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.TextView;
        import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float maxJerk;
    private float x,y,z;
    private SharedPreferences sharePref;
    private long lastUpdate = 0;
    private TextView jerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set up the accelerometer
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalizeViews();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sharePref = PreferenceManager.getDefaultSharedPreferences(this);

        Integer test = Integer.getInteger(sharePref.getString("position_in_vehicle", "-1"));

        maxJerk = 10*test;

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);


    }

    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    public void onClick2(View v) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }


    public void initalizeViews(){
        jerk = (TextView) findViewById(R.id.jerk);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {



            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 400) {
                long diffTime = (curTime - lastUpdate);

                float prevX = x;
                float prevY = y;
                float prevZ = z;

                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                double jerk1 = (java.lang.Math.sqrt(java.lang.Math.pow(x,2) + java.lang.Math.pow(y,2) +
                        java.lang.Math.pow(z,2)) - java.lang.Math.sqrt(java.lang.Math.pow(prevX,2) +
                        java.lang.Math.pow(prevY,2) + java.lang.Math.pow(prevZ,2)))/(0.4);

                lastUpdate = curTime;


                jerk.setText(Double.toString(jerk1));

                if (java.lang.Math.sqrt(java.lang.Math.pow(jerk1,2)) > maxJerk){
                    View testView = findViewById(R.id.progressBar2);
                    testView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                else{
                    View testView = findViewById(R.id.progressBar2);
                    testView.setBackgroundColor(getResources().getColor(android.R.color.white));
                }
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

    public void jerk_calc (float xx, float yy, float zz){

    }
}



