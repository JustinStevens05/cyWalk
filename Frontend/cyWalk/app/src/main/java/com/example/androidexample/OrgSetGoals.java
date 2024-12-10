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
 * The organizations view that allows them to set and modify their goals for their organization
 * */
public class OrgSetGoals extends AppCompatActivity {

    private Button usersButton;
    private Button leaderboardButton;
    private Button goalsButton;
    private Button profileButton;
    private Button newGoalBtn;
    private String key;
    private String orgId="";
    private TextView currentGoalDist;
    private TextView currentGoalReward;
    private EditText newGoalDist;
    private EditText newGoalReward;

    private static String URL_GET_GOAL = null;
    private static String URL_SET_GOAL = null;

    /**
     * creates the page that allows the organization to make and manage their goals and their rewards
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orgsetgoals);
        currentGoalDist = findViewById(R.id.currentGoalDist);
        currentGoalReward = findViewById(R.id.currentGoalReward);
        newGoalDist = findViewById(R.id.newGoalDist);
        newGoalReward = findViewById(R.id.newGoalReward);
        newGoalBtn = findViewById(R.id.newGoalBtn);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        orgId = extras.getString("orgId");

        //URL_SET_GOAL = "http://10.0.2.2:8080/friends/"+key;

        newGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //also currently waiting on the urls
                //try {
                //    setJsonObjNewGoals();
                //} catch (JSONException e) {
                //    throw new RuntimeException(e);
                //}
                //getJsonObjCurrentGoal();
            }
        });

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.orgbottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_org_users) {
                intent = new Intent(OrgSetGoals.this, OrgUsers.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_goals) {
                intent = new Intent(OrgSetGoals.this, OrgSetGoals.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_social) {
                intent = new Intent(OrgSetGoals.this, OrgLeaderboards.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_profile) {
                intent = new Intent(OrgSetGoals.this, OrgProfile.class);
                intent.putExtra("key", key);
                intent.putExtra("orgId",orgId);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });

        //currently waiting on the url
        //getJsonObjCurrentGoal();
    }

    private void getJsonObjCurrentGoal() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_GOAL,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            // Parse JSON object data
                            String distance = response.getString("distance");
                            String reward = response.getString("reward");

                            currentGoalDist.setText(distance);
                            currentGoalReward.setText(reward);

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

    private void setJsonObjNewGoals() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newDistance", Integer.parseInt(newGoalDist.getText().toString()));
        jsonObject.put("newReward", Integer.parseInt(newGoalReward.getText().toString()));
        final String requestBody = jsonObject.toString();
        //title.setText(requestBody);
        //title.setTextSize(10);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_SET_GOAL,
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