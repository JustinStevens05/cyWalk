package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * The organizations view of their organizations leaderboard
 * */
public class OrgLeaderboards extends AppCompatActivity {

    private Button usersButton;
    private Button leaderboardButton;
    private Button goalsButton;
    private Button profileButton;
    private String key;
    private String orgId="";

    private static String URL_ORG_LEADERBOARD = null;

    /**
     *builds the page for the organizations to view their organization leaderboard
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orgleaderboards);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("key");
        orgId = extras.getString("orgId");

        //URL_SET_GOAL = "http://10.0.2.2:8080/friends/"+key;

        // NAVIGATION BAR
        BottomNavigationView botnav = findViewById(R.id.orgbottomNavigation);
        botnav.setSelectedItemId(R.id.nav_social);
        botnav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            if (item.getItemId() == R.id.nav_org_users) {
                intent = new Intent(OrgLeaderboards.this, OrgUsers.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_goals) {
                intent = new Intent(OrgLeaderboards.this, OrgSetGoals.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_social) {
                intent = new Intent(OrgLeaderboards.this, OrgLeaderboards.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_org_profile) {
                intent = new Intent(OrgLeaderboards.this, OrgProfile.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
                return true;
            }
            else {
                return false;
            }
        });

        //setNewGoalReq();
    }

}