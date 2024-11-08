package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback, WebSocketListener {

    private String key = "";
    private final int FINE_PERMISSION_CODE = 1;
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
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isTracking = false;
    String mobile_url_chunk;
    String local_url_chunk;

    private String URL_JSON_GET_DISTANCE = null;
    private String URL_JSON_GET_USER = null;
    private String URL_JSON_POST_LOCATION = null;
    private String URL_WS_SOCKET = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);                             // link to Main activity XML
        txt_daily_distance = findViewById(R.id.txt_daily_distance);
        txt_greeting = findViewById(R.id.txt_greeting);
        txt_websocket_test = findViewById(R.id.txt_websocket_test);
        btn_start_auto_route = findViewById(R.id.btn_start_auto_route);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");

        mobile_url_chunk = "coms-3090-072.class.las.iastate.edu:8080";
        local_url_chunk = "10.0.2.2:8080";

        URL_JSON_GET_DISTANCE = "http://" + mobile_url_chunk + "/"+key+"/locations/total";
        URL_JSON_GET_USER = "http://" + mobile_url_chunk + "/users/"+key;
        URL_JSON_POST_LOCATION = "http://" + mobile_url_chunk + "/"+key+"/locations/createLocation";
        URL_WS_SOCKET = "ws://" + mobile_url_chunk + "/locations/sessions?key="+key;

        /* connect this activity to the websocket instance */
        WebSocketManagerLocation.getInstance().setWebSocketListener(Dashboard.this);

        // Establish WebSocket connection and set listener
        WebSocketManagerLocation.getInstance().connectWebSocket(URL_WS_SOCKET);

        /* click listener on auto route button pressed */
       btn_start_auto_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isTracking = !isTracking;
                //txt_websocket_test.setText("Connected");
                for (int counter = 0; counter < 50; counter++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //txt_greeting.setText("Latitude: " + currentLocation.getLatitude() + "\n" + "Longitude: " + currentLocation.getLongitude());
                        jsonObject.put("latitude", currentLocation.getLatitude());
                        jsonObject.put("longitude", currentLocation.getLongitude());
                        jsonObject.put("elevation", 0);
                        runOnUiThread(() -> {
                            WebSocketManagerLocation.getInstance().sendMessage(jsonObject);
                        });
                        LatLng currentCoords = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        gMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoords));
                        currentLocation.setLatitude(currentLocation.getLatitude() + 0.05);
                        currentLocation.setLongitude(currentLocation.getLongitude() + 0.05);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
       });

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
        makeJsonObjReq();
        getLastLocation();


    }

    private void getLastLocation() {
        // if permissions not already granted, request permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);
                    mapFragment.getMapAsync(Dashboard.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Location permission is disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        requestDailyDistance();
        LatLng currentCoords = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        gMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoords));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentCoords, 6.0f));
    }

    // For getting distance double
    private void requestDailyDistance() {
        String URL_JSON_GET_DISTANCE = "http://" + mobile_url_chunk + "/"+key+"/location/total";
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
     * Sends a string request [GET] without a body.
     */
    private void makeJsonObjReq() {
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

    /*
     * Methods implementing WebSocketListener
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) throws InterruptedException {
        //txt_websocket_test.setText("Connected");
    }

    @Override
    public void onWebSocketMessage(String message) throws InterruptedException {
        txt_daily_distance.setText("Daily Distance: " + message);

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {
        //txt_websocket_test.setText("WS Error");
    }
}