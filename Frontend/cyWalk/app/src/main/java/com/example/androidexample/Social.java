package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Social extends AppCompatActivity {


    private Button goalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social);
        goalButton = findViewById(R.id.goalsBtn);

        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(Social.this, Goals.class);
                startActivity(intent);
            }
        });
    }
}