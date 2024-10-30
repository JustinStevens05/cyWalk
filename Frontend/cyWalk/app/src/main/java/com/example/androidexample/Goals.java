package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Goals extends AppCompatActivity {

    private TextView daily_step_disp;
    private TextView weekly_step_disp;
    private TextView title;
    private ProgressBar daily_bar;
    private ProgressBar weekly_bar;
    private int dailyStepCount = 0;
    private int weeklyStepCount = 0;
    private int dailyGoal = 10000;
    private int weeklyGoal = 70000;
    // private Button socialButton;
    private Button newGoalsButton;
    private Button submitButton;
    private EditText newDaily;
    private EditText newWeekly;
    private LinearLayout newGoalLayout;
    private String key;
    private String username;

    private static String URL_JSON_OBJECT = null;
    private static String URL_NEW_GOALS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        //BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        //botnav.setSelectedItemId(R.id.nav_goals);

        // socialButton = findViewById(R.id.socialBtn);
        newGoalsButton = findViewById(R.id.setGoalsBtn);
        newDaily = findViewById(R.id.new_daily);
        newWeekly = findViewById(R.id.new_weekly);
        submitButton =findViewById(R.id.submitBtn);
        newGoalLayout = findViewById(R.id.newGoalLayout);
        daily_step_disp = findViewById(R.id.dailySteps);
        weekly_step_disp = findViewById(R.id.weeklySteps);
        daily_bar = findViewById(R.id.dailyprogressBar);
        weekly_bar = findViewById(R.id.weeklyprogressBar);
        title = findViewById(R.id.title);

        daily_bar.setMax(dailyGoal);
        weekly_bar.setMax(weeklyGoal);

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                intent = new Intent(Goals.this, Dashboard.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_goals) {
                intent = new Intent(Goals.this, Goals.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_social) {
                intent = new Intent(Goals.this, Social.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(Goals.this, Profile.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });



        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        username = extras.getString("username");
        URL_JSON_OBJECT = "https://a7d1bdb7-5276-4165-951c-f32dee760766.mock.pstmn.io/users?userId=1";
        URL_NEW_GOALS = "http://10.0.2.2:8080/goals/" + username;

//        socialButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                /* when counter button is pressed, use intent to switch to Counter Activity */
//                Intent intent = new Intent(Goals.this, Social.class);
//                intent.putExtra("key", key);
//                startActivity(intent);
//            }
//        });

        newGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGoalLayout.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setJsonObjStepGoals();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                dailyGoal = Integer.parseInt(newDaily.getText().toString());
                weeklyGoal = Integer.parseInt(newWeekly.getText().toString());

                daily_step_disp.setText(dailyStepCount + "/" + dailyGoal);
                weekly_step_disp.setText(weeklyStepCount + "/" + weeklyGoal);

                daily_bar.setMax(dailyGoal);
                weekly_bar.setMax(weeklyGoal);

                newDaily.setText("");
                newWeekly.setText("");
                newGoalLayout.setVisibility(View.INVISIBLE);
            }
        });

        getJsonObjStepGoals();
    }
    private void getJsonObjStepGoals() {
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
    private void setJsonObjStepGoals() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dailyGoal", Integer.parseInt(newDaily.getText().toString()));
        jsonObject.put("weeklyGoal", Integer.parseInt(newWeekly.getText().toString()));
        final String requestBody = jsonObject.toString();
        //title.setText(requestBody);
        //title.setTextSize(10);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_NEW_GOALS,
                jsonObject,
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