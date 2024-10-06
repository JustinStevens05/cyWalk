package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);             // link to Main activity XML

        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_dashboard);

        botnav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_dashboard:
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                    return true;
                case R.id.nav_goals:
                    startActivity(new Intent(getApplicationContext(), Goals.class));
                    finish();
                    return true;
                case R.id.nav_social:
                    startActivity(new Intent(getApplicationContext(), Social.class));
                    finish();
                    return true;
                case R.id.nav_profile:
                    return true;
            }
            return false;
        });
    }
}
