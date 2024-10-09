package com.example.androidexample;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);             // link to Main activity XML

        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_dashboard);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);



        botnav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_dashboard:
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
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    finish();
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng coord = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions().position(coord).title("Coord Variable"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
    }
}
