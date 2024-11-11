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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrgProfile extends AppCompatActivity {

    private static String key;
    private static String URL_JSON_OBJECT = null;
    private static String URL_CREATE_ORG = null;
    private static String URL_FIND_ORG = null;
    private String username;
    private String orgId = "";
    private String orgName;
    private Button usersButton;
    private Button leaderboardButton;
    private Button goalsButton;
    private Button profileButton;
    private Button createOrgButton;
    private Button findOrgButton;
    private TextView txt_username;
    private EditText createOrgName;
    private EditText findOrgName;


    /**
     * creates the profile page for the organization users
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orgprofile);
        usersButton = findViewById(R.id.usersButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);
        goalsButton = findViewById(R.id.goalsButton);
        profileButton = findViewById(R.id.profileButton);
        createOrgButton = findViewById(R.id.createOrg);
        findOrgButton = findViewById(R.id.getOrg);
        createOrgName = findViewById(R.id.newOrgName);
        findOrgName = findViewById(R.id.findOrgName);


        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        orgId = extras.getString("orgId");
        //txt_response.setText("Key: " + key);
        URL_JSON_OBJECT = "http://10.0.2.2:8080/users/"+key;
        txt_username = findViewById(R.id.profile_txt_username);

        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgProfile.this, orgUsers.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgProfile.this, orgLeaderboards.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
            }
        });

        goalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgProfile.this, orgSetGoals.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgProfile.this, OrgProfile.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
            }
        });

        createOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orgName = createOrgName.getText().toString();
                URL_CREATE_ORG = "http://10.0.2.2:8080/organizations";
                try {
                    makeOrgReq();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        findOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orgName = findOrgName.getText().toString();
                URL_FIND_ORG = "http://10.0.2.2:8080/organizations/get-id";
                try {
                    findOrgReq();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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

    /**
     * takes the input from the new organization name box and attempts to create a new organization with that name.
     * if successful sets the orgId var equal to the newly created organization's Id number
     */
    private void makeOrgReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", orgName);

        txt_username.setText(jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_CREATE_ORG, jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            orgId = response.getString("id");
                            txt_username.setText(orgId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        //txt_username.setText(error.toString());
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
     * takes the input from the organization lookup box and sets the orgId var to the id associated with that organizations
     */
    private void findOrgReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", orgName);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_FIND_ORG, jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            orgId = response.getString("id");
                            txt_username.setText(orgId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        //txt_username.setText(error.toString());
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
