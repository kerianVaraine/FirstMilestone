package com.example.firstmilestone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private final float[] magnetometerReading = new float[3]; //Azimuth (z-rotation), Pitch (x-rotation), Roll(y-axis)
    private TextView t_azimuth;
    private TextView t_pitch;
    private TextView t_roll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set Refs here to send to view
        t_azimuth = (TextView) findViewById(R.id.text_azimuth);
        t_pitch = (TextView) findViewById(R.id.text_pitch);
        t_roll = (TextView) findViewById(R.id.text_roll);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //pause updates to log
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        float magX = event.values[0];
        t_azimuth.setText("azimuth(z):\n" + magX);
        float magY = event.values[1];
        t_pitch.setText("pitch(x):\n" + magY);
        float magZ = event.values[2];
        t_roll.setText("roll(y):\n" + magZ);



        // t_azimuth = magnetometerReading[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}


//find view by id -- send floata to id
//findViewById(R.id.text_azimuth);