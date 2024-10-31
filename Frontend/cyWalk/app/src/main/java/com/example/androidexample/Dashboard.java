package com.example.androidexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback {

    public static String URL_STRING_REQ;
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap gMap;
    private static String key;
    private String totalDistance;
    private String URL_JSON_OBJECT = "http://10.0.2.2:8080/users/"+key;
    TextView txt_daily_distance;
    String username;
    TextView txt_greeting;
    TextView txt_response;
    TextView txt_coords;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);                             // link to Main activity XML
        txt_daily_distance = findViewById(R.id.txt_daily_distance);
        txt_greeting = findViewById(R.id.txt_greeting);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        txt_response.setText("Key: " + key);
        URL_JSON_OBJECT = "http://10.0.2.2:8080/users/"+key;
        URL_STRING_REQ = "http://10.0.2.2:8080/"+key+"/location/total";

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
        else {
            txt_coords.setText("Permissions enabled");
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    txt_coords.setText("(" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ")");
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
        LatLng currentCoords = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        txt_coords.setText("(" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + ")");
        gMap.addMarker(new MarkerOptions().position(currentCoords).title("Current Location"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(currentCoords));

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                LatLng markerCoords = new LatLng(50, 50);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("New Marker");
                gMap.addMarker(markerOptions);
                gMap.moveCamera(CameraUpdateFactory.newLatLng(markerCoords));
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("latitude", markerCoords.latitude);
                    jsonObject.put("longitude", markerCoords.longitude);
                    jsonObject.put("elevation", 0);
                    // URL_JSON_OBJECT_REQ = "http://10.0.2.2:8080/"+key+"/location/total";
                    makeStringReqWithBody(jsonObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // For posting location
    private void makeStringReqWithBody(JSONObject jsonBody) {
        final String mRequestBody = jsonBody.toString();

        URL_STRING_REQ = "http://10.0.2.2:8080/"+key+"/location/total";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_STRING_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        // msgResponse.setText(response.toString());

                        txt_daily_distance.setText(response.toString());
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
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL_JSON_OBJECT, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            username = response.getString("username");
                            // key = response.getString("key");

                            // Populate text views with the parsed data
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
    };


}



