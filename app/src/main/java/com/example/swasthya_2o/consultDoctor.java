package com.example.swasthya_2o;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class consultDoctor extends AppCompatActivity implements PaymentResultListener {
    private Button payment_btn;
    private EditText problemDescription;
    Spinner departmentSpinner,availabilitySpinner,specialistSpinner;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    PatientModel model;
    String selectedItem="";

    String[] medicalDepartments = {"Select Department", "Dentistry", "Surgery", "Cardiology", "Orthopedics", "Neurology", "Ophthalmology", "Otolaryngology", "Gastroenterology", "Pediatrics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_doctor);

        Checkout.preload(getApplicationContext());
        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        payment_btn=findViewById(R.id.btn_appointment);
        problemDescription=findViewById(R.id.problemdescription);
        departmentSpinner=findViewById(R.id.departmentSpinner);
        availabilitySpinner=findViewById(R.id.availabilitySpinner);
        specialistSpinner=findViewById(R.id.selectSpecialist);

        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  patientproblem();
                  //startPayment();
            }
        });

    }

    private void patientproblem() {
        String userProblem;
        String userChosenTime="",userChosenSpecialist="";
        Boolean flag=false;

        userProblem=problemDescription.getText().toString();
        if(userProblem.isEmpty() ){
            Toast.makeText(this, "Describe your problem", Toast.LENGTH_SHORT).show();
            return;
        }

        //department
//        String[] items=getResources().getStringArray(R.array.department);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicalDepartments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = medicalDepartments[position];
                model.setUserChosenDepartment(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(consultDoctor.this, "Select Department", Toast.LENGTH_SHORT).show();
            }
        });

        model=new PatientModel(userProblem,userChosenSpecialist,userChosenTime);
        startPayment();

    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_v2wpucW2vEehtH");
        checkout.setImage(R.drawable.hearthealth);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Satya");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Appointment Successful", Toast.LENGTH_SHORT).show();

        String uid=auth.getUid();

        firebaseDatabase.getReference().child("Patient Problem").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(consultDoctor.this, "Record submitted", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Transaction failed,do registration again", Toast.LENGTH_SHORT).show();
    }
}