package com.example.androidexample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The organizations view that shows them all users in their organizations
 * */
public class OrgUsers extends AppCompatActivity {

    private String key;
    private String orgId="";
    private LinearLayout body;
    private TextView title;

    private static String URL_ORG_USERS = null;

    /**
     * creates the page that shows the organization all the users included in their organization
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orgusers);
        body = findViewById(R.id.body);
        title = findViewById(R.id.title);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("id");
        orgId = extras.getString("orgId");

        URL_ORG_USERS = "http://coms-3090-072.class.las.iastate.edu:8080/organizations/"+orgId+"/users";

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.orgbottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_org_users) {
                intent = new Intent(OrgUsers.this, OrgUsers.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_goals) {
                intent = new Intent(OrgUsers.this, OrgSetGoals.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_social) {
                intent = new Intent(OrgUsers.this, OrgLeaderboards.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_profile) {
                intent = new Intent(OrgUsers.this, OrgProfile.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });

        getUsersReq();
    }

    /**
     * gets the list of all uses that are in the organization. Takes the returned list of person objects and just prints out their
     * usernames to the screen
     */
    private void getUsersReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_ORG_USERS,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        //usersText.setText(response.toString());

                        if(response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject current = response.getJSONObject(i);
                                    String user = current.getString("username");

                                    LinearLayout tempLayout = new LinearLayout(OrgUsers.this);
                                    tempLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    ));

                                    tempLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    tempLayout.setPadding(10,10,10,10);

                                    TextView tempText = new TextView(OrgUsers.this);
                                    tempText.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));

                                    tempText.setTextSize(20);
                                    tempText.setTextColor(Color.parseColor("#000000"));
                                    tempText.setPadding(25,25,25,25);
                                    tempText.setText(user);

                                    tempLayout.addView(tempText);
                                    body.addView(tempLayout);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }

}