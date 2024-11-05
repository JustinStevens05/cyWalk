package com.example.androidexample;

import static java.util.Objects.isNull;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Friends extends AppCompatActivity {

    private Button backButton;
    private Button friendsSubmitButton;
    private Button acceptFriendSubmitButton;
    private EditText friendUsername;
    private EditText acceptedFriendUsername;
    private TextView newFriendTitle;
    private String key;
    private LinearLayout friendTable;
    private LinearLayout requestsTable;

    private static String URL_JSON_FRIENDS = null;
    private static String URL_JSON_PENDING = null;
    private static String URL_NEW_FRIEND = null;
    private static String URL_ACCEPT_FRIEND = null;

    private String newFriendUsername;
    private String acceptFriendUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        backButton = findViewById(R.id.returnButton);
        friendsSubmitButton = findViewById(R.id.friendSubmitBtn);
        acceptFriendSubmitButton = findViewById(R.id.acceptFriendSubmitBtn);
        friendTable = findViewById(R.id.friendsTable);
        requestsTable = findViewById(R.id.requestsTable);
        friendUsername = findViewById(R.id.friendUsername);
        acceptedFriendUsername = findViewById(R.id.acceptedFriendUsername);
        newFriendTitle = findViewById(R.id.newFriendTile);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");

        URL_JSON_FRIENDS = "http://10.0.2.2:8080/friends/"+key;
        URL_JSON_PENDING = "http://10.0.2.2:8080/friends/requests/"+key;


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Friends.this, Social.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        friendsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFriendUsername = friendUsername.getText().toString();
                URL_NEW_FRIEND = "http://10.0.2.2:8080/friends/" + key +"/request/" + newFriendUsername;

                makeFriendRequest();
            }
        });

        acceptFriendSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptFriendUsername = acceptedFriendUsername.getText().toString();
                URL_ACCEPT_FRIEND = "http://10.0.2.2:8080/friends/" + key +"/request/approve/" + acceptFriendUsername;

                makeFriendApproval();
            }
        });

        makeJsonFriendReq();
        makeJsonPendingReq();
    }
    private void makeJsonFriendReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_FRIENDS,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        //newFriendTitle.setText(response.toString());

                        if(response.length() > 0) {
                            friendTable.removeAllViews();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    String current = response.getString(i);
                                    TextView tempText = new TextView(Friends.this);
                                    tempText.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));

                                    tempText.setTextSize(20);
                                    tempText.setTextColor(Color.parseColor("#000000"));
                                    tempText.setText(current);

                                    friendTable.addView(tempText);

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

    private void makeJsonPendingReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_PENDING,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        if(response.length() > 0) {
                            requestsTable.removeAllViews();
                            for (int i = 0; i < response.length(); i++) {

                                try {
                                    String current = response.getString(i);
                                    TextView tempText = new TextView(Friends.this);

                                    tempText.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tempText.setTextSize(20);
                                    tempText.setTextColor(Color.parseColor("#000000"));
                                    tempText.setText(current);

                                    requestsTable.addView(tempText);

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

    private void makeFriendRequest() {
        //JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(

                Request.Method.POST,
                URL_NEW_FRIEND,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        newFriendTitle.setText("Request sent successfully would you like to add another friend");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        friendUsername.setText("");
                        //temp.setText(error.toString());
                        //newFriendTitle.setText("couldn't find that user try again");
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

    private void makeFriendApproval() {
        //JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(

                Request.Method.PUT,
                URL_ACCEPT_FRIEND,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        acceptedFriendUsername.setText("");
                        //newFriendTitle.setText(error.toString());
                        //newFriendTitle.setText("couldn't find that user try again");
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