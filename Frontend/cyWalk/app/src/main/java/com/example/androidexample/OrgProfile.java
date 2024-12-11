package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The organizations view of their own profile
 * */
public class OrgProfile extends AppCompatActivity {

    private static String key;
    private static String URL_JSON_OBJECT = null;
    private static String URL_CREATE_ORG = null;
    private static String URL_FIND_ORG = null;
    private String username;
    private String orgId = "";
    private String orgName;
    private TextView txt_username;


    /**
     * creates the profile page for the organization users
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orgprofile);


        Bundle extras = getIntent().getExtras();
        key = extras.getString("id");
        orgId = extras.getString("orgId");
        //txt_response.setText("Key: " + key);
        URL_JSON_OBJECT = "http://10.0.2.2:8080/users/"+key;
        txt_username = findViewById(R.id.profile_txt_username);

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.orgbottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_org_users) {
                intent = new Intent(OrgProfile.this, OrgUsers.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_goals) {
                intent = new Intent(OrgProfile.this, OrgSetGoals.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_social) {
                intent = new Intent(OrgProfile.this, OrgLeaderboards.class);
                intent.putExtra("id", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_profile) {
                intent = new Intent(OrgProfile.this, OrgProfile.class);
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

        makeUsernameReq();
    }

    /**
     * gets the organizations username using their session key and sets the username text box equal to their username
     */
    private void makeUsernameReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL_JSON_OBJECT, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            username = response.getString("username");
                            txt_username.setText(username);
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
}
