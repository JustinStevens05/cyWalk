package com.example.androidexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {

    private static final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private GoogleMap gMap;
    private Location currentLocation;
    private Marker userMarker; // Marker for the user's location
    private boolean isCameraMoved = false; // To track if the camera was moved initially

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize location updates callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        currentLocation = location;
                        updateMapWithLocation(location);
                    }
                }
            }
        };

        getLastLocation();
    }

    /**
     * Get the last known location of the user.
     */
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                initializeMap();
            }
        });
    }

    /**
     * Initialize the map and set up tracking.
     */
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Creates the location request and starts location updates.
     */
    private void createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setMinUpdateIntervalMillis(1500)
                .build();

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        settingsClient.checkLocationSettings(settingsBuilder.build())
                .addOnSuccessListener(locationSettingsResponse -> startLocationUpdates(locationRequest))
                .addOnFailureListener(e -> Toast.makeText(this, "Please enable location services.", Toast.LENGTH_SHORT).show());
    }

    /**
     * Starts location updates with the given location request.
     */
    private void startLocationUpdates(LocationRequest locationRequest) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    /**
     * Updates the map with the user's current location and moves the marker.
     */
    private void updateMapWithLocation(Location location) {
        if (gMap != null) {
            LatLng currentCoords = new LatLng(location.getLatitude(), location.getLongitude());

            if (userMarker == null) {
                // Add a marker at the user's current location
                userMarker = gMap.addMarker(new MarkerOptions().position(currentCoords).title("You are here"));
                if (!isCameraMoved) {
                    // Move the camera only the first time
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoords, 20.0f));
                    isCameraMoved = true;
                }
            } else {
                // Update the existing marker's position
                userMarker.setPosition(currentCoords);
            }
        }
    }

    /**
     * Handle the result of permission requests.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * When the map is ready, set the current location and start location updates.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if (currentLocation != null) {
            updateMapWithLocation(currentLocation);
        }
        createLocationRequest();
    }
}


//@Override
//    public void onWebSocketOpen(ServerHandshake handshakedata) {}
//
//    @Override
//    public void onWebSocketMessage(String message) {}
//
//    @Override
//    public void onWebSocketClose(int code, String reason, boolean remote) {}
//
//    @Override
//    public void onWebSocketError(Exception ex) {}
