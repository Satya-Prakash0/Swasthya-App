package com.example.swasthya_2o;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navmenu);
        drawerLayout = findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new homeFragment()).commit();
        nav.setCheckedItem(R.id.menu_home);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        temp = new homeFragment();
                        break;

                    case R.id.videoCall:
                        Intent intentcall = new Intent(MainActivity.this, videoCalling.class);
                        startActivity(intentcall);
                        break;

                    case R.id.chatting:
                        Intent intent4 = new Intent(MainActivity.this, groupChat.class);
                        startActivity(intent4);
                        break;

                    case R.id.bmi:
                        Intent intent = new Intent(MainActivity.this, bodyMassIndex.class);
                        startActivity(intent);
                        break;

                    case R.id.stepCounter:
                        Intent intentstep = new Intent(MainActivity.this, stepsensor.class);
                        startActivity(intentstep);
                        break;

                    case R.id.emergency:
                        Intent intentemer = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intentemer);
                        break;

                    case R.id.logout:
                        auth.signOut();
                        Intent intent2 = new Intent(MainActivity.this, signIn.class);
                        startActivity(intent2);
                        finish();
                        break;

                    case R.id.menu_setting:
                        Toast.makeText(MainActivity.this, "Under working condition!", Toast.LENGTH_SHORT).show();
                        // Handle setting menu
                        break;

                    case R.id.aboutTheApp:
                        Intent intentaboutapp=new Intent(MainActivity.this,AboutTheApp.class);
                        startActivity(intentaboutapp);
                        break;

                    case R.id.Share:
                        shareTheApp();
                        break;
                }

                if (temp != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, temp).commit();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void shareTheApp() {
        String appLink = "https://play.google.com/store/apps/details?id=com.example.swasthya_2o";

        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Check out the Swasthya App");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey there! I wanted to share the Swasthya App with you. Download it: \n" + appLink);
        startActivity(Intent.createChooser(intent,"Share via"));

    }
}
