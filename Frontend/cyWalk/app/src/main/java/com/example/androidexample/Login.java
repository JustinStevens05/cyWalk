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

public class Login extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private TextView errorMsg;
    private Button loginButton;    // define login button variable
    private static String URL_JSON_OBJECT = null;
    private String userKey = "";
    private String username;
    private String password;
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

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                URL_JSON_OBJECT = "http://10.0.2.2:8080/users";

                try {
                    makeJsonObjReq();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(Login.this, Dashboard.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });

        /* click listener on sign up button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                //String requestBody = "{\"name\": \"", \"data\": {\"price\": 400, \"color\": \"Purple\"}}";

                /* when sign up button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(Login.this, Dashboard.class);
                intent.putExtra("USERNAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
    }
    private void makeJsonObjReq() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        final String requestBody = jsonObject.toString();

        //errorMsg.setText(requestBody);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL_JSON_OBJECT,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            userKey = response.getString("key");
                            //extraMsg.setText("working " + userKey);
                            if(!userKey.isEmpty()) {
                                Intent intent = new Intent(Login.this, Social.class);
                                intent.putExtra("key", userKey);
                                intent.putExtra("username",username);
                                //errorMsg.setText("success " + userKey);
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