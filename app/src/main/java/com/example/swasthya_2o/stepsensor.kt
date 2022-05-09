package com.example.swasthya_2o

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLog
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.util.*

class stepsensor : AppCompatActivity(),SensorEventListener {
    
    private var sensorManager: SensorManager?=null
    
    private var running=false
    private var totalSteps=0f
    private var previousTotalSteps=0f
     private lateinit var tv_step_taken :TextView
    private lateinit var progress_circular:CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stepsensor)
          tv_step_taken = findViewById(R.id.tv_step_taken) as TextView
          progress_circular = findViewById(R.id.progress_circular) as CircularProgressBar
        loadData()
        resetSteps()
        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running=true;
        val stepSensor :Sensor? =sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if(stepSensor==null){
            Toast.makeText(this,"No sensor",Toast.LENGTH_LONG).show()
        }
        else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            totalSteps =event!!.values[0];
            val currentSteps : Int= totalSteps.toInt() -previousTotalSteps.toInt()
            tv_step_taken.text=("$currentSteps")

            progress_circular.apply{
                setProgressWithAnimation(50F)
            }
        }
    }
    private  fun resetSteps(){
        tv_step_taken.setOnClickListener{
            Toast.makeText(this,"Long tap to reset",Toast.LENGTH_SHORT).show()
        }
        tv_step_taken.setOnLongClickListener {
            previousTotalSteps=totalSteps
            tv_step_taken.text=0.toString()
            saveData()

            true
        }
    }
    private fun saveData(){
        val sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
        val editor =sharedPreferences.edit()
        editor.putFloat("key1",previousTotalSteps)
        editor.apply()
    }
    private fun loadData(){
        val sharedPreferences = getSharedPreferences("myPref",Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1",0f)
        Log.d("Activity","$savedNumber")
        previousTotalSteps=savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show()
    }
}


