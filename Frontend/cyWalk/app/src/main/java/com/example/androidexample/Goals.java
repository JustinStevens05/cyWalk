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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Goals page for the users to see
 * */
public class Goals extends AppCompatActivity {

    private TextView daily_step_disp;
    private TextView weekly_step_disp;
    private TextView title;
    private TextView orgGoalTitle;
    private ProgressBar daily_bar;
    private ProgressBar weekly_bar;
    private int dailyStepCount = 0;
    private int weeklyStepCount = 0;
    private int dailyGoal = 10000;
    private int weeklyGoal = 70000;
    private Button newGoalsButton;
    private Button submitButton;
    private Button newPlanButton;
    private EditText newDaily;
    private EditText newWeekly;
    private LinearLayout newGoalLayout;
    private RelativeLayout addPlanLayout;
    private String key;
    private String username;
    private String userType;

    private static String URL_GET_GOALS = null;
    private static String URL_NEW_GOALS = null;
    private static String URL_GET_USERNAME = null;
    private static String URL_GET_DAILY_DIST = null;
    private static String URL_GET_WEEKLY_DIST = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        //BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        //botnav.setSelectedItemId(R.id.nav_goals);

        newGoalsButton = findViewById(R.id.setGoalsBtn);
        newDaily = findViewById(R.id.new_daily);
        newWeekly = findViewById(R.id.new_weekly);
        submitButton =findViewById(R.id.submitBtn);
        newPlanButton = findViewById(R.id.newPlanBtn);
        newGoalLayout = findViewById(R.id.newGoalLayout);
        daily_step_disp = findViewById(R.id.dailySteps);
        weekly_step_disp = findViewById(R.id.weeklySteps);
        daily_bar = findViewById(R.id.dailyprogressBar);
        weekly_bar = findViewById(R.id.weeklyprogressBar);
        title = findViewById(R.id.title);
        orgGoalTitle = findViewById(R.id.orgGoalTitle);
        addPlanLayout = findViewById(R.id.addPlanBtnLayout);

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
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_goals) {
                intent = new Intent(Goals.this, Goals.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_social) {
                intent = new Intent(Goals.this, Social.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(Goals.this, Profile.class);
                intent.putExtra("key", key);
                intent.putExtra("userType", userType);
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
        userType = extras.getString("userType");
        URL_GET_USERNAME = "http://coms-3090-072.class.las.iastate.edu:8080/users/"+key;
        URL_GET_WEEKLY_DIST = "http://coms-3090-072.class.las.iastate.edu:8080/"+key+"/locations/week/total";

        if(userType.equals("guest")){
            orgGoalTitle.setVisibility(View.INVISIBLE);
            addPlanLayout.setVisibility(View.INVISIBLE);
        }

        newPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToJoinOrg();
            }
        });

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

                daily_step_disp.setText("0 /" + dailyGoal);
                weekly_step_disp.setText("0 /" + weeklyGoal);

                daily_bar.setMax(dailyGoal);
                weekly_bar.setMax(weeklyGoal);

                newDaily.setText("");
                newWeekly.setText("");
                newGoalLayout.setVisibility(View.INVISIBLE);
            }
        });

        requestUsername();
    }

    private void requestUsername() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL_GET_USERNAME, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            username = response.getString("username");
                            URL_GET_GOALS = "http://coms-3090-072.class.las.iastate.edu:8080/goals/" + username;
                            URL_NEW_GOALS = "http://coms-3090-072.class.las.iastate.edu:8080/goals/" + username;
                            URL_GET_DAILY_DIST = "http://coms-3090-072.class.las.iastate.edu:8080/0/locations/user/"+username+"/total";

                            getJsonObjStepGoals();

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
     *gets the current step goals for the user
     */
    private void getJsonObjStepGoals() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_GOALS,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            String dsteps = response.getString("dailyGoal");
                            String wsteps = response.getString("weeklyGoal");

                            dailyGoal = Integer.parseInt(dsteps);
                            weeklyGoal = Integer.parseInt(wsteps);

                            // Populate text views with the parsed data
                            daily_step_disp.setText("0 /" + dailyGoal);
                            weekly_step_disp.setText("0 /" + weeklyGoal);

                            daily_bar.setProgress(dailyStepCount);
                            weekly_bar.setProgress(weeklyStepCount);

                            getJsonObjDailyDist();
                            getJsonObjWeeklyDist();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        daily_step_disp.setText("set goals below");
                        weekly_step_disp.setText("set goals below");
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
     *updates the users step goals locally and changes their goals that are stored in the database
     */
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
                        weekly_step_disp.setText(error.toString());
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
     *gets the users username based off of the session key
     *switches to the join organizations page and passes in the session key and the retrieved username
     */
    private void switchToJoinOrg() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, URL_GET_USERNAME, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            username = response.getString("username");

                            Intent intent = new Intent(Goals.this, OrganizationLookUp.class);
                            intent.putExtra("key", key);
                            intent.putExtra("username", username);
                            intent.putExtra("userType", userType);
                            startActivity(intent);

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

    private void getJsonObjDailyDist() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_DAILY_DIST,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            String dDist = response.getString("totalDistance");

                            // Populate text views with the parsed data
                            daily_step_disp.setText(dDist + "/" + dailyGoal);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        daily_step_disp.setText("0.0/" + dailyGoal);
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

    private void getJsonObjWeeklyDist() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL_GET_WEEKLY_DIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful response here
                        Log.d("Volley Response", response);
                        weekly_step_disp.setText(response + "/" + weeklyGoal);
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