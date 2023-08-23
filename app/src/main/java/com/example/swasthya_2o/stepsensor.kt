package com.example.swasthya_2o

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class stepsensor : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps=0f;
    private var initialSteps = 0f
    private lateinit var tv_step_taken: TextView
    private lateinit var progress_circular: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stepsensor)

        tv_step_taken = findViewById(R.id.tv_step_taken)
        progress_circular = findViewById(R.id.progress_circular)

        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true

        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No step counter sensor available", Toast.LENGTH_LONG).show()
        } else {

            initialSteps = previousTotalSteps
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            val currentSteps: Int = (event!!.values[0] - initialSteps).toInt()
            tv_step_taken.text = currentSteps.toString()

            progress_circular.apply {
                val maxSteps = 1000  // Adjust based on your expected max steps
                val progressPercentage = ((currentSteps.toFloat()*8) / maxSteps) * 100
                setProgressWithAnimation(progressPercentage)
            }
        }
    }

    private fun resetSteps() {
        tv_step_taken.setOnClickListener {
            Toast.makeText(this, "Long tap to reset", Toast.LENGTH_SHORT).show()
        }
        tv_step_taken.setOnLongClickListener {
            initialSteps = totalSteps
            tv_step_taken.text = "0"
            progress_circular.apply { setProgressWithAnimation(0F) }
            saveData()

            true
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", initialSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        initialSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for step counter sensor
    }
}
