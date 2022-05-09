package com.example.swasthya_2o;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class registration extends AppCompatActivity {

    private TextView alreadyHaveAcc;
    private Button doctor_reg;
    private Button patient_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        alreadyHaveAcc=findViewById(R.id.AlreadyHaveAcc);
        doctor_reg=findViewById(R.id.doctor_reg);
        patient_reg=findViewById(R.id.patient_reg);

        patient_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registration.this,patient_registration.class);
                startActivity(intent);
                finish();
            }
        });

        doctor_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registration.this,doctor_registration.class);
                startActivity(intent);
                finish();
            }
        });

        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registration.this,signIn.class);
                startActivity(intent);
                finish();
            }
        });
    }
}