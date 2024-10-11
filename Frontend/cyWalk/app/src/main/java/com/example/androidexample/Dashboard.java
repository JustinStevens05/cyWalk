package com.example.androidexample;


import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import static java.time.LocalTime.now;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {

    public static String URL_STRING_REQ = "";
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap gMap;
    private static String key;
    private String totalDistance;
    private String URL_JSON_OBJECT_LOCATION = "";
    private String URL_JSON_OBJECT_USER = "";
    TextView txt_daily_distance;
    String username;
    TextView txt_greeting;
    TextView txt_response;
    // Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);             // link to Main activity XML
        txt_daily_distance = findViewById(R.id.txt_daily_distance);
        txt_greeting = findViewById(R.id.txt_greeting);
        txt_response = findViewById(R.id.txt_response);

        // GOOGLE MAP FRAGMENT
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_dashboard);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");

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
        makeJsonObjReq();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng currentCoords = new LatLng(50, 50);
        gMap.addMarker(new MarkerOptions().position(currentCoords).title("Current Location"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoords));

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                gMap = googleMap;
                LatLng markerCoords = new LatLng(50, 50);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("New Marker");
                gMap.addMarker(markerOptions);
                gMap.moveCamera(CameraUpdateFactory.newLatLng(markerCoords));

                try {
                    // fields should match the attributes of the User Object at:
                    // https://git.las.iastate.edu/cs309/tutorials/-/blob/springboot_unit2_1_onetoone/springboot_example/src/main/java/onetoone/Users/User.java
                    JSONObject jsonBody = new JSONObject();

                    jsonBody.put("latitude", markerCoords.latitude);
                    jsonBody.put("longitude", markerCoords.longitude);
                    jsonBody.put("elevation", 0);
                    makeStringReqWithBody(jsonBody);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                makeJsonObjReq();
            }
        });
    }

    // For posting location
    private void makeStringReqWithBody(JSONObject jsonBody) {
        final String mRequestBody = jsonBody.toString();

        URL_STRING_REQ = "http://10.0.2.2:8080/"+key+"/locations/createLocation";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, URL_STRING_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        // msgResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                        // msgResponse.setText(error.toString());
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.d("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
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

        /*
        JSON Object Request for location
         */
        URL_JSON_OBJECT_LOCATION = "http://10.0.2.2:8080/"+key+"/location/total";
        URL_JSON_OBJECT_USER = "http://10.0.2.2:8080/users/"+key;
        JsonObjectRequest jsonObjReqLocation = new JsonObjectRequest(
                Request.Method.GET, URL_JSON_OBJECT_LOCATION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response here
                        txt_response.setText(Log.d("Volley Response", response.toString()));
                        try {
                            // Parse JSON object data
                            totalDistance = response.getString("totalDistance");

                            // Populate text views with the parsed data
                            txt_daily_distance.setText("Daily Distance: \n" + totalDistance);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        txt_response.setText(Log.e("Volley Error", error.toString()));
                    }
                }
        );

        /*
        JSON Object request for user information
         */
        JsonObjectRequest jsonObjReqUser = new JsonObjectRequest(
                Request.Method.GET, URL_JSON_OBJECT_USER, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response here
                        txt_response.setText(Log.d("Volley Response", response.toString()));
                        try {
                            // Parse JSON object data
                            username = response.getString("username");

                            // Populate text views with the parsed data
                            txt_greeting.setText("Welcome Back " + username + "!");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        txt_response.setText(Log.e("Volley Error", error.toString()));
                    }
                }
        ){
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReqLocation);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReqUser);
    }

}



