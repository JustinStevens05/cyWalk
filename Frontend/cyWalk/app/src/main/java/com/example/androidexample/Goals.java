package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Goals extends AppCompatActivity {

    private TextView daily_step_disp;
    private TextView weekly_step_disp;
    private ProgressBar daily_bar;
    private ProgressBar weekly_bar;
    private int dailyStepCount = 0;
    private int weeklyStepCount = 0;
    private final int dailyGoal = 10000;
    private final int weeklyGoal = 70000;
    private Button socialButton;
    private String key;

    private static String URL_JSON_OBJECT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        socialButton = findViewById(R.id.socialBtn);
        daily_step_disp = findViewById(R.id.dailySteps);
        weekly_step_disp = findViewById(R.id.weeklySteps);
        daily_bar = findViewById(R.id.dailyprogressBar);
        weekly_bar = findViewById(R.id.weeklyprogressBar);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");


        URL_JSON_OBJECT = "https://a7d1bdb7-5276-4165-951c-f32dee760766.mock.pstmn.io/users?userId=1";

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(Goals.this, Social.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        makeJsonObjReq();
    }
    private void makeJsonObjReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_JSON_OBJECT,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            String dsteps = response.getString("dailySteps");
                            String wsteps = response.getString("weeklySteps");

                            dailyStepCount = Integer.parseInt(dsteps);
                            weeklyStepCount = Integer.parseInt(wsteps);

                            // Populate text views with the parsed data
                            daily_step_disp.setText(dailyStepCount + "/" + dailyGoal);
                            weekly_step_disp.setText(weeklyStepCount + "/" + weeklyGoal);

                            daily_bar.setProgress(dailyStepCount);
                            weekly_bar.setProgress(weeklyStepCount);

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