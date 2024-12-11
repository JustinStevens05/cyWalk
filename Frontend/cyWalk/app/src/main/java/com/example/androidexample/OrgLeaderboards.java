package com.example.androidexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The organizations view of their organizations leaderboard
 * */
public class OrgLeaderboards extends AppCompatActivity {

    private String key;
    private String orgId="";
    private TextView title;
    ListView lb_listView;
    private ArrayList<String> lbList;

    private static String URL_ORG_LEADERBOARD = null;
    private static String URL_USER_IMAGE = null;

    /**
     *builds the page for the organizations to view their organization leaderboard
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orgleaderboards);
        title = findViewById(R.id.title);
        lb_listView = findViewById(R.id.lv_leaderboard);

        lbList = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        orgId = extras.getString("orgId");

        URL_ORG_LEADERBOARD = "http://coms-3090-072.class.las.iastate.edu:8080/organizations/" + orgId + "/leaderboard";

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.orgbottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_org_users) {
                intent = new Intent(OrgLeaderboards.this, OrgUsers.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_goals) {
                intent = new Intent(OrgLeaderboards.this, OrgSetGoals.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_social) {
                intent = new Intent(OrgLeaderboards.this, OrgLeaderboards.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_profile) {
                intent = new Intent(OrgLeaderboards.this, OrgProfile.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });

        globalLeaderboardReq();
    }

    private void globalLeaderboardReq() {

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET, URL_ORG_LEADERBOARD, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        if(response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject current = response.getJSONObject(i);
                                    String userEntry = current.getString("username") + " | Distance: " +
                                            current.getString("totalSteps");
                                    lbList.add(userEntry);
                                    //String current = response.getString(i);
                                    //lbList.add(current);
                                    //lbList.add(response.getString(0));

                                    TextView tempText = new TextView(OrgLeaderboards.this);
                                    tempText.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));

                                    tempText.setTextSize(20);
                                    tempText.setTextColor(Color.parseColor("#000000"));
                                    //tempText.setText(current);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    lbList.add(e.toString());
                                }
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrgLeaderboards.this, R.layout.list_item_leaderboard, R.id.entryUsername, lbList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = convertView;

                                // If the view is null, inflate the custom layout
                                if (view == null) {
                                    view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_leaderboard, parent, false);
                                }

                                // Get references to the views in the custom layout
                                TextView usernameTextView = view.findViewById(R.id.entryUsername);
                                TextView distanceTextView = view.findViewById(R.id.entryDistance);
                                ImageView profileImageView = view.findViewById(R.id.entryImage);

                                // Set data for each item (example: lbList stores "username | distance")
                                String userEntry = lbList.get(position);
                                String[] parts = userEntry.split(" \\| ");
                                String username = parts[0];
                                String distance = parts[1];

                                // Set the username and distance text
                                usernameTextView.setText(username);
                                distanceTextView.setText(distance); // Correct distance format

                                URL_USER_IMAGE = "http://coms-3090-072.class.las.iastate.edu:8080/users/image/"+username;

                                ImageRequest imageRequest = new ImageRequest(
                                        URL_USER_IMAGE,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                // Display the image in the ImageView
                                                profileImageView.setImageBitmap(response);
                                            }
                                        },
                                        0, // Width, set to 0 to get the original width
                                        0, // Height, set to 0 to get the original height
                                        ImageView.ScaleType.FIT_XY, // ScaleType
                                        Bitmap.Config.RGB_565, // Bitmap config

                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Handle errors here
                                                Log.e("Volley Error", error.toString());
                                            }
                                        }
                                );

                                // Adding request to request queue
                                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);

                                return view;
                            }

                        };

// Set the adapter to the ListView
                        lb_listView.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        //title.setText("");
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }
}