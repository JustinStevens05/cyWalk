package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Goals extends AppCompatActivity {

    private TextView daily_step_disp;
    private TextView weekly_step_disp;
    private int dailyStepCount = 1000;
    private int weeklyStepCount = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        daily_step_disp = findViewById(R.id.dailySteps);
        weekly_step_disp = findViewById(R.id.weeklySteps);

        daily_step_disp.setText(dailyStepCount + "/10000");
        weekly_step_disp.setText(weeklyStepCount + "/70000");


    }
}