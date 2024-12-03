package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Dashboard class for the users
 * */
public class Dashboard extends AppCompatActivity implements OnMapReadyCallback, WebSocketListener {

    private String key = "";
    private static final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private GoogleMap gMap;
    private String totalDistance;
    TextView txt_daily_distance;
    TextView txt_websocket_test;
    String username;
    double latitude;
    double longitude;
    TextView txt_greeting;
    private Button btn_start_auto_route;
    private Button btn_increment_auto_route;
    private SwitchCompat switch_auto_route;
    Location currentLocation;
    private boolean isTracking = false;
    String server_url_chunk;
    String local_url_chunk;
    LatLng currentCoords;
    Handler handler;
    long locationTick = 3000;
    private Runnable locationRunnable;
    private Handler locationHandler = new Handler();
    private Marker userMarker; // Marker for the user's location
    private boolean isCameraMoved = false; // To track if the camera was moved initially

    private String URL_JSON_GET_DISTANCE = null;
    private String URL_JSON_GET_USER = null;
    private String URL_JSON_POST_LOCATION = null;
    private String URL_WS_SOCKET = null;

    /**
     * creates the dashboard view for for the user to see
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);                             // link to Main activity XML
        txt_daily_distance = findViewById(R.id.txt_daily_distance);
        txt_greeting = findViewById(R.id.txt_greeting);
        btn_start_auto_route = findViewById(R.id.btn_start_auto_route);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setupLocationCallback();

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");

        server_url_chunk = "coms-3090-072.class.las.iastate.edu:8080";
        local_url_chunk = "10.0.2.2:8080";
        URL_JSON_GET_DISTANCE = "http://coms-3090-072.class.las.iastate.edu:8080/"+key+"/locations/total";
        URL_JSON_GET_USER = "http://coms-3090-072.class.las.iastate.edu:8080/users/"+key;
        URL_JSON_POST_LOCATION = "http://coms-3090-072.class.las.iastate.edu:8080/"+key+"/locations/createLocation";
        URL_WS_SOCKET = "ws://coms-3090-072.class.las.iastate.edu:8080/locations/sessions?key="+key;

        /* connect this activity to the websocket instance */
        WebSocketManagerLocation.getInstance().setWebSocketListener(Dashboard.this);

        // Establish WebSocket connection and set listener
        WebSocketManagerLocation.getInstance().connectWebSocket(URL_WS_SOCKET);

        // Initialize location runnable
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTracking && currentLocation != null) {
                    sendLocationThroughWebSocket(currentLocation);
                    locationHandler.postDelayed(this, locationTick); // Repeat every LOCATION_TICK ms
                }
            }
        };

        /* click listener on auto route button pressed */
        btn_start_auto_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTracking = !isTracking; // Toggle tracking state
                if (isTracking) {
                    Toast.makeText(Dashboard.this, "Auto-route started", Toast.LENGTH_SHORT).show();
                    createLocationRequest();
                    locationHandler.postDelayed(locationRunnable, locationTick);
                } else {
                    Toast.makeText(Dashboard.this, "Auto-route stopped", Toast.LENGTH_SHORT).show();
                    stopLocationUpdates();
                    locationHandler.removeCallbacks(locationRunnable);
                }
            }
        });

        initializeMap();

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                intent = new Intent(Dashboard.this, Dashboard.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_goals) {
                intent = new Intent(Dashboard.this, Goals.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_social) {
                intent = new Intent(Dashboard.this, Social.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(Dashboard.this, Profile.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });
        requestUsername();
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
     * When the map is ready this will move the assign the gMap object to the google map. Then it will place the users
     * pinpoint on the map and move the map to ge centered over their location.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if(currentLocation != null) {
            updateMapWithLocation(currentLocation);
        }

        createLocationRequest();
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
     * Retrieves the last known location of the user from the database. If the permission is not already granted for
     * the program to get the location it will then request permission.
     */
    private void startLocationUpdates(LocationRequest locationRequest) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    /**
     * Updates the map with the user's current location and moves the marker.
     */
    private void updateMapWithLocation(Location location) {
        if (gMap != null) {
            LatLng currentCoords = new LatLng(location.getLatitude(), location.getLongitude());
            if (userMarker == null) {
                userMarker = gMap.addMarker(new MarkerOptions().position(currentCoords).title("You are here"));
                if (!isCameraMoved) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCoords, 20.0f));
                    isCameraMoved = true;
                }
            } else {
                userMarker.setPosition(currentCoords);
            }
        }
    }

    private void sendLocationThroughWebSocket(Location location) {
        if(location != null && !isTracking) {
            return;
        }

        if(isTracking) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("latitude", location.getLatitude());
                jsonObject.put("longitude", location.getLongitude());
                jsonObject.put("elevation", 0);
                WebSocketManagerLocation.getInstance().sendMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    currentLocation = locationResult.getLastLocation();
                    updateMapWithLocation(currentLocation);

                    if (isTracking) {
                        sendLocationThroughWebSocket(currentLocation); // Only send location if tracking
                    }
                }
            }
        };
    }

    /**
     * Handle the result of permission requests.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isTracking) {
                    createLocationRequest();
                }
            } else {
                Toast.makeText(this, "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Asks the backend server for the distance that the user has walked so far in the day using a volley request.
     * retrieves the double from the database and then changes the text on the screen to reflect this new distance.
     */
    private void requestDailyDistance() {
        String URL_JSON_GET_DISTANCE = "http://" + server_url_chunk + "/"+key+"/location/total";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_JSON_GET_DISTANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        // msgResponse.setText(response.toString());
                        txt_daily_distance.setText("Daily Distance: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        // msgResponse.setText(error.toString());
                    }
                }
            ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    /**
     * requests the username from the database using the session key and then sets the welcome text equal to the username retrieved
     */
    private void requestUsername() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL_JSON_GET_USER, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            username = response.getString("username");
                            // key = response.getString("key");
                            txt_greeting.setText(username);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }

    /**
     * required websocket code currently does nothing
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) throws InterruptedException {
        //txt_websocket_test.setText("Connected");
    }

    /**
     *updates the daily distance on the screen when the websocket returns a value
     */
    @Override
    public void onWebSocketMessage(String message) throws InterruptedException {
        double receivedDistance = Double.parseDouble(message);
        txt_daily_distance.setText("Daily Distance: " + String.format("%.1f", receivedDistance));

    }

    /**
     * required websocket code currently does nothing
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    /**
     *required websocket code currently does nothing
     */
    @Override
    public void onWebSocketError(Exception ex) {
        //txt_websocket_test.setText("WS Error");
    }
}