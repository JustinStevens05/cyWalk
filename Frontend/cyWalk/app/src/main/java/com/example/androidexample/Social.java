package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Social extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    myViewPagerAdapter myViewPagerAdapter;
    TextView title;

    private Button goalButton;
    private String key;
    private String username;

    private static String URL_JSON_OBJECT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social);
        // goalButton = findViewById(R.id.goalsBtn);
        title = findViewById(R.id.title);

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
        makeJsonObjReq();



        URL_JSON_OBJECT = "http://10.0.2.2:8080/users/"+key;

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

        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Social.this, Goals.class);
                intent.putExtra("key", key);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        makeJsonObjReq();
    }
    private void makeJsonObjReq() {
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
}