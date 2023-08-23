package com.example.swasthya_2o;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_screen);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, signIn.class);
            startActivity(intent);
            finish();
        }

//        Intent intent = new Intent(this, signIn.class);
//        startActivity(intent);
//        finish();
    }

}