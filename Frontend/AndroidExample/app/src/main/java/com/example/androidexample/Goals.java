package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Goals extends AppCompatActivity {

    private TextView daily_step_disp;
    private TextView weekly_step_disp;
    private ProgressBar daily_bar;
    private ProgressBar weekly_bar;
    private int dailyStepCount = 1000;
    private int weeklyStepCount = 5000;
    private final int dailyGoal = 10000;
    private final int weeklyGoal = 70000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        daily_step_disp = findViewById(R.id.dailySteps);
        weekly_step_disp = findViewById(R.id.weeklySteps);
        daily_bar = findViewById(R.id.dailyprogressBar);
        weekly_bar = findViewById(R.id.weeklyprogressBar);

        daily_step_disp.setText(dailyStepCount + "/" + dailyGoal);
        weekly_step_disp.setText(weeklyStepCount + "/" + weeklyGoal);

        daily_bar.setProgress(dailyStepCount);
        weekly_bar.setProgress(weeklyStepCount);

    }
}