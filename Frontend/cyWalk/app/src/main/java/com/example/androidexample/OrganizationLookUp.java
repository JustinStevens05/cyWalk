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
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrganizationLookUp extends AppCompatActivity {

    private Button backButton;
    private Button orgJoinBtn;
    private String key;
    private String orgId;
    private String orgName = "";
    private String username = "";
    private JSONArray orgs;
    private EditText org_name;
    private TextView title;

    private static String URL_ORGANIZATIONS_JOIN= null;
    private static String URL_ORGANIZATIONS_FIND= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_look_up);
        backButton = findViewById(R.id.returnButton);
        orgJoinBtn = findViewById(R.id.orgSubmitBtn);
        org_name = findViewById(R.id.org_name_edit);
        title = findViewById(R.id.title);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        username = extras.getString("username");

        URL_ORGANIZATIONS_FIND = "http://10.0.2.2:8080/organizations/get-id";


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizationLookUp.this, Goals.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        orgJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orgName = org_name.getText().toString();

                try {
                    makeJsonOrgNameReq();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

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
                            URL_ORGANIZATIONS_JOIN = "http://10.0.2.2:8080/organizations/"+ orgId +"/join";
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
}