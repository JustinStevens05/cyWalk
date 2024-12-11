package com.example.androidexample;

import static java.util.Objects.isNull;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Page for the users to lookup organizations they could join
 * */
public class OrganizationLookUp extends AppCompatActivity {

    private Button backButton;
    private String key;
    private String orgId;
    private String orgName = "";
    private String username = "";
    private JSONArray orgs;
    private TextView title;
    private String userType;
    private LinearLayout body;

    private static String URL_ORGANIZATIONS_JOIN= null;
    private static String URL_ORGANIZATIONS_FIND= null;
    private static String URL_GET_ALL_ORGS = null;
    private String URL_GOAL_SWITCH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_look_up);
        backButton = findViewById(R.id.returnButton);
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        userType = extras.getString("userType");
        username = extras.getString("username");

        URL_ORGANIZATIONS_FIND = "http://coms-3090-072.class.las.iastate.edu:8080/organizations/get-id";
        URL_GET_ALL_ORGS = "http://coms-3090-072.class.las.iastate.edu:8080/organizations/all";
        URL_GOAL_SWITCH = "http://coms-3090-072.class.las.iastate.edu:8080/users/" + key + "/organization";


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrg();
            }
        });

        getAllOrgsReq();
    }

    private void getAllOrgsReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_ALL_ORGS,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        if(response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject current = response.getJSONObject(i);
                                    String orgEntry = current.getString("name");

                                    LinearLayout tempLayout = new LinearLayout(OrganizationLookUp.this);
                                    tempLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    ));

                                    tempLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    tempLayout.setPadding(10,10,10,10);

                                    TextView tempText = new TextView(OrganizationLookUp.this);
                                    tempText.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));

                                    tempText.setTextSize(20);
                                    tempText.setTextColor(Color.parseColor("#000000"));
                                    tempText.setText(orgEntry);

                                    Button tempButton = new Button(OrganizationLookUp.this);
                                    tempButton.setText("Join");

                                    tempButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                orgName = current.getString("name");
                                                makeJsonOrgNameReq();
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });

                                    tempLayout.addView(tempText);
                                    tempLayout.addView(tempButton);
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
                }) {
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }

    /**
     *requests to join the organization that the user requests
     */
    private void makeJsonOrgJoinReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_ORGANIZATIONS_JOIN,
                jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        //title.setText();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        //title.setText(URL_ORGANIZATIONS_JOIN);
                        Intent intent = new Intent(OrganizationLookUp.this, Goals.class);
                        intent.putExtra("key", key);
                        intent.putExtra("userType", userType);
                        startActivity(intent);
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
     *gets the organization id of the organization that the user has requested and then calls the makeJsonOrgJoinReq function
     * using the retrieved id number
     */
    private void makeJsonOrgNameReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", orgName);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_ORGANIZATIONS_FIND, jsonObject, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            orgId = response.getString("id");
                            URL_ORGANIZATIONS_JOIN = "http://coms-3090-072.class.las.iastate.edu:8080/organizations/"+ orgId +"/join";
                            //title.setText(orgId + "  " + username);
                            makeJsonOrgJoinReq();
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
     * requests the organization of the user if they are part of one
     */
    private void getOrg() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL_GOAL_SWITCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        Intent intent = new Intent(OrganizationLookUp.this, Goals.class);
                        intent.putExtra("key", key);
                        intent.putExtra("userType", userType);
                        intent.putExtra("orgName", response);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
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
}