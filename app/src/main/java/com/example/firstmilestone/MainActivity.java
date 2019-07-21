package com.example.firstmilestone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    //magnetometer inits
    private final float[] magnetometerReading = new float[3]; //Azimuth (z-rotation), Pitch (x-rotation), Roll(y-axis)
    private TextView t_azimuth;
    private TextView t_pitch;
    private TextView t_roll;
    private float magX, magY, magZ;

    //GPS inits
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private TextView t_lat;
    private TextView t_long;
    private String lat;
    private String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;


    //

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Magnetometer
        //set Refs here to send to view
        t_azimuth = (TextView) findViewById(R.id.text_azimuth);
        t_pitch = (TextView) findViewById(R.id.text_pitch);
        t_roll = (TextView) findViewById(R.id.text_roll);

        //GPS
        t_lat = (TextView) findViewById(R.id.text_lat);
        t_long = (TextView) findViewById(R.id.text_long);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
        magX = event.values[0];
        t_azimuth.setText("azimuth(z):\n" + magX);
        magY = event.values[1];
        t_pitch.setText("pitch(x):\n" + magY);
        magZ = event.values[2];
        t_roll.setText("roll(y):\n" + magZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //Logging to CSV
    public void saveLogToFile(View view) {
        String FILENAME = "magno_data.csv";
        String entry = magX + "," + magY + "," + magZ + "\n";

        //append entry to csv file
        try {
            FileOutputStream out = openFileOutput(FILENAME, Context.MODE_APPEND);
            out.write( entry.getBytes() );
            out.close();
            toastIt("Entry Saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //User feedback to show file has been saved
    private void toastIt( String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    //LOCATION LISTENER
    @Override
    public void onLocationChanged(Location location) {
        t_lat.setText("Lat:" + location.getLatitude());
        t_long.setText("Long:" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "Disabled");
    }
}

