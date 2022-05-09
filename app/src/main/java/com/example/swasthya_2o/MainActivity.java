package com.example.swasthya_2o;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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

        auth=FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=findViewById(R.id.navmenu);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new homeFragment()).commit();
        nav.setCheckedItem(R.id.menu_home);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch(item.getItemId()){

                    case R.id.menu_home:
                        Toast.makeText(MainActivity.this, "home pannel is open", Toast.LENGTH_SHORT).show();
                        temp=new homeFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.videoCall:
                        Toast.makeText(MainActivity.this, "video call", Toast.LENGTH_SHORT).show();
                        Intent intentcall=new Intent(MainActivity.this,videoCalling.class);
                        startActivity(intentcall);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.chatting:
                        Toast.makeText(MainActivity.this, "chatting", Toast.LENGTH_SHORT).show();
                        Intent intent4=new Intent(MainActivity.this,groupChat.class);
                        startActivity(intent4);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.bmi:
                        Toast.makeText(MainActivity.this, "BMI", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,bodyMassIndex.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.stepCounter:
                        Toast.makeText(MainActivity.this, "step counter", Toast.LENGTH_SHORT).show();
                        Intent intentstep=new Intent(MainActivity.this,stepsensor.class);
                        startActivity(intentstep);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.emergency:
                        Toast.makeText(MainActivity.this, "emergency", Toast.LENGTH_SHORT).show();
                        Intent intentemer=new Intent(MainActivity.this,MapsActivity.class);
                        startActivity(intentemer);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.logout:
                        Toast.makeText(MainActivity.this, "Power", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        Intent intent2=new Intent(MainActivity.this,signIn.class);
                        startActivity(intent2);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_setting:
                        Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,temp).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
}