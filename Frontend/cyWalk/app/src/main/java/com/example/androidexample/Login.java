package com.example.androidexample;

import static java.lang.Double.isNaN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Login page for all users to see upon opening the app
 * */
public class Login extends AppCompatActivity {

    EditText usernameEditText;  // define username edittext variable
    EditText passwordEditText;  // define password edittext variable
    private TextView errorMsg;
    private TextView orgSwitch;
    private TextView guestSwitch;
    Button loginButton;         // define login button variable
    private String server_url_chunk = "coms-3090-072.class.las.iastate.edu:8080";
    private String local_url_chunk = "10.0.2.2:8080";
    private String URL_LOGIN = null;
    private String URL_SIGNUP = null;
    private String key = "";
    private String username;
    private String password;
    private String userType = "";
    private Button signUpButton;        // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);            // link to Login activity XML

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        errorMsg = findViewById(R.id.errorMsg);
        signUpButton = findViewById(R.id.login_signup_btn);
        orgSwitch = findViewById(R.id.switchOrgView);
        guestSwitch = findViewById(R.id.switchGuestView);

        URL_LOGIN = "http://coms-3090-072.class.las.iastate.edu:8080/users";
        URL_SIGNUP = "http://coms-3090-072.class.las.iastate.edu:8080/signup";

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                try {
                    makeLoginReq();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /* click listener on sign up button pressed */
        // Sign up is post
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                try {
                    makeSignUpReq();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        orgSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, OrgLogin.class);
                startActivity(intent);
            }
        });

        guestSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, GuestLogin.class);
                startActivity(intent);
            }
        });
    }

    /**
     * attempts to log the user in using the inputted username and password if correct will  switch their view
     * to the dashboard if incorrect will throw an error and send a message to the user to let them know that something
     * was wrong.
     */
    private void makeLoginReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        final String requestBody = jsonObject.toString();

        //errorMsg.setText(requestBody);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT, URL_LOGIN, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response after login attempt: ", response.toString());
                        try {
                            // Parse JSON object data
                            key = response.getString("id");
                            userType = response.getString("type");
                            //extraMsg.setText("working " + userKey);
                            if(!key.isEmpty()) {
                                Intent intent = new Intent(Login.this, Dashboard.class);
                                intent.putExtra("key", key);
                                intent.putExtra("userType", userType);
                                //errorMsg.setText("success " + key);
                                startActivity(intent);
                            } else {
                                //errorMsg.setText("failed " + userKey);
                                errorMsg.setText("Error invalid username/password");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        errorMsg.setText("Error invalid username/password");
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
     * attempts to create a new user using the entered credentials. if the new user is created it will move the user to the dashboard page
     * if the username is already in use it will throw and error and tell the user that something went wrong.
     */
    private void makeSignUpReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        //final String requestBody = jsonObject.toString();
        //errorMsg.setText(requestBody);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, URL_SIGNUP, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            key = response.getString("id");
                            userType = response.getString("type");
                            //extraMsg.setText("working " + userKey);
                            if(!key.isEmpty()) {
                                Intent intent = new Intent(Login.this, Dashboard.class);
                                intent.putExtra("key", key);
                                intent.putExtra("userType", userType);
                                //errorMsg.setText("success " + userKey);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        errorMsg.setText("User already exists");
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