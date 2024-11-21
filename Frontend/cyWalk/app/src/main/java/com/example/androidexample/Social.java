package com.example.androidexample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The users socials view that shows them the three types of leaderboards and where they rank
 * */
public class Social extends AppCompatActivity implements WebSocketListener{

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    myViewPagerAdapter myViewPagerAdapter;
    TextView title;
    TextView temp;
    ListView lb_listView;

    private LinearLayout leaderbaordTester;
    private Button goalButton;
    private Button friendsButton;
    private String key;
    private String username;
    private ArrayList<String> lbList;

    private static String URL_JSON_OBJECT = null;
    private static String URL_GLOBAL_LEADERBOARD = null;
    private String URL_WS_SOCKET = null;

    /**
     * creates the social page for the user to see
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social);
        friendsButton = findViewById(R.id.friendsButton);
        title = findViewById(R.id.title);
        temp = findViewById(R.id.temp);
        lb_listView = findViewById(R.id.lv_leaderboard);

        lbList = new ArrayList<String>();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        myViewPagerAdapter = new myViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.bottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_dashboard) {
                intent = new Intent(Social.this, Dashboard.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_goals) {
                intent = new Intent(Social.this, Goals.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_social) {
                intent = new Intent(Social.this, Social.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(Social.this, Profile.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });
        //makeUsernameReq();

        URL_JSON_OBJECT = "http://coms-3090-072.class.las.iastate.edu:8080/users/"+key;
        URL_GLOBAL_LEADERBOARD = "http://coms-3090-072.class.las.iastate.edu:8080/leaderboard";
        URL_WS_SOCKET = "ws://coms-3090-072.class.las.iastate.edu:8080/locations/friends?key="+key;

        /* connect this activity to the websocket instance */
        WebSocketManagerLeaderboard.getInstance().setWebSocketListener(Social.this);

        // Establish WebSocket connection and set listener
        WebSocketManagerLeaderboard.getInstance().connectWebSocket(URL_WS_SOCKET);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Social.this, Friends.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        makeUsernameReq();
        globalLeaderboardReq();
    }

    /**
     * gets the current users username based off of their session key and then includes that name into the title of the page
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

                            // Populate text views with the parsed data
                            title.setText(username + "'S SOCIAL");

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
     * retrieves the global leaderboard from the database and then prints it out onto the screen
     */
    private void globalLeaderboardReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET, URL_GLOBAL_LEADERBOARD, null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        if(response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject current = response.getJSONObject(i);
                                    String userEntry = current.getString("username") + " | Distance: " +
                                            current.getString("totalSteps");
                                    lbList.add(userEntry);
                                    //String current = response.getString(i);
                                    //lbList.add(current);
                                    //lbList.add(response.getString(0));

                                    TextView tempText = new TextView(Social.this);
                                    tempText.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));

                                    tempText.setTextSize(20);
                                    tempText.setTextColor(Color.parseColor("#000000"));
                                    //tempText.setText(current);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    lbList.add(e.toString());
                                }
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter(Social.this, R.layout.lb_textview, R.id.tv_leaderboard, lbList);
                        lb_listView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        //title.setText("not good");
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

    /*
     * Methods implementing WebSocketListener
     */
    /**
     * required websocket code currently does nothing
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        runOnUiThread(() -> {
            //temp.setText("Websocket Connected");
        });
    }

    /**
     * prints out the retrieved leaderboard from the database and updates it is the leaderboard changes
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            //temp.setText("Websocket did something");
        });
    }

    /**
     * required websocket code currently does nothing
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        runOnUiThread(() -> {
            //temp.setText("Websocket closed");
        });
    }

    /**
     * required websocket code currently does nothing
     */
    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> {
            //temp.setText(ex.toString());
        });
    }
}