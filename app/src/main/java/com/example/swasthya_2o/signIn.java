package com.example.swasthya_2o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signIn extends AppCompatActivity {

    private TextView clickForSignUp,AdminLogIn;
    private EditText email,password;
    Button signIn;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        clickForSignUp=findViewById(R.id.clickForsignup);
        email=findViewById(R.id.emailSignIn);
        password=findViewById(R.id.passwordSignIn);
        signIn=findViewById(R.id.btn_signin);
        AdminLogIn=findViewById(R.id.AdminLogin);

        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Login to account...");

        clickForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signIn.this,registration.class);
                startActivity(intent);
                finish();
            }
        });
        AdminLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(signIn.this, "Under working condition!!", Toast.LENGTH_SHORT).show();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL=email.getText().toString();
                String PASSWORD=password.getText().toString();
                if(!EMAIL.isEmpty() && !PASSWORD.isEmpty()) {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signIn.this, "Logging is succssfull", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(signIn.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(signIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(signIn.this, "Text field is empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}