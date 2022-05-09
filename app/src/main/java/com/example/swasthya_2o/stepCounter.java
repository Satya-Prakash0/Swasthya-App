package com.example.swasthya_2o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class stepCounter extends AppCompatActivity implements SensorEventListener {

    TextView tv_steps;
    SharedPreferences sharedPreferences;
    SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        tv_steps=findViewById(R.id.tv_steps);
        String currentDate=new SimpleDateFormat("DD-MM-YYYY", Locale.getDefault()).format(new Date());

        sharedPreferences=this.getSharedPreferences("com.example.swasthya_2o", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed;

        sensorManager =(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        Sensor countSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(countSensor !=null ){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }
        else{
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
        if(!sharedPreferences.contains(currentDate)){
            sharedPreferences.edit().putInt(currentDate,0).apply();
        }
        if(sharedPreferences.contains(currentDate)){
            int v=sharedPreferences.getInt(currentDate,1);
            tv_steps.setText(String.valueOf(v));
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        String currentdate=new SimpleDateFormat("DD-MM-YYYY",Locale.getDefault()).format(new Date());

        if(sharedPreferences.contains(currentdate)){
            int value=sharedPreferences.getInt(currentdate,1);
            value+=1;
            sharedPreferences.edit().putInt(currentdate,value).apply();
            Toast.makeText(this, String.valueOf(value), Toast.LENGTH_SHORT).show();
            tv_steps.setText(String.valueOf(value));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}