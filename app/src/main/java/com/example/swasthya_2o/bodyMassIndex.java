package com.example.swasthya_2o;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class bodyMassIndex extends AppCompatActivity {

    private EditText bmiAge,bmiHeight,bmiWeight;
    Spinner bmigender;
    Button bmiCheckbtn,bmiDietBtn;
    public float BMIVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_mass_index);

        bmiAge=findViewById(R.id.bmiAge);
        bmiHeight=findViewById(R.id.bmiHeight);
        bmigender=findViewById(R.id.bmigender);
        bmiWeight=findViewById(R.id.bmiWeight);

        bmiCheckbtn=findViewById(R.id.bmiCheckbtn);
        bmiDietBtn=findViewById(R.id.bmiDietBtn);


        bmiCheckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String age=bmiAge.getText().toString().trim();
                final String weight=bmiWeight.getText().toString().trim();
                final String height=bmiHeight.getText().toString().trim();
                final String gender=bmigender.getSelectedItem().toString();

                if(age.isEmpty() || weight.isEmpty() || height.isEmpty() || gender.equalsIgnoreCase("Choose Gender"))
                    Toast.makeText(bodyMassIndex.this, "Empty field", Toast.LENGTH_SHORT).show();

                else{
                      BMIVal= Float.parseFloat(weight) / (float)Math.pow(Float.parseFloat(height),2);
                      String status=" ";
                      if(BMIVal < 18) status="Underweight";
                      else if(BMIVal>18 && BMIVal<24) status="Normal Weight";
                      else if(BMIVal>20 && BMIVal<29)  status="Overweight" ;
                      else status="Obese";

                    Toast.makeText(bodyMassIndex.this, "Your BMI is: "+BMIVal +"\n You are :"+status, Toast.LENGTH_SHORT).show();

                }
            }
        });

        bmiDietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String age=bmiAge.getText().toString().trim();
                final String weight=bmiWeight.getText().toString().trim();
                final String height=bmiHeight.getText().toString().trim();
                final String gender=bmigender.getSelectedItem().toString();

                if(age.isEmpty() || weight.isEmpty() || height.isEmpty() || gender.equalsIgnoreCase("Choose Gender"))
                    Toast.makeText(bodyMassIndex.this, "Empty field", Toast.LENGTH_SHORT).show();

                else {
                    Intent intent = new Intent(bodyMassIndex.this, DietPlan.class);
                    startActivity(intent);
                }
            }
        });

    }
}