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
    private int weeklyStepCount = 51000;
    private final int dailyGoal = 10000;
    private final int weeklyGoal = 70000;
    private Button socialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        socialButton = findViewById(R.id.socialBtn);
        daily_step_disp = findViewById(R.id.dailySteps);
        weekly_step_disp = findViewById(R.id.weeklySteps);
        daily_bar = findViewById(R.id.dailyprogressBar);
        weekly_bar = findViewById(R.id.weeklyprogressBar);

        daily_step_disp.setText(dailyStepCount + "/" + dailyGoal);
        weekly_step_disp.setText(weeklyStepCount + "/" + weeklyGoal);

        daily_bar.setProgress(dailyStepCount);
        weekly_bar.setProgress(weeklyStepCount);

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(Goals.this, Social.class);
                startActivity(intent);
            }
        });
    }
}