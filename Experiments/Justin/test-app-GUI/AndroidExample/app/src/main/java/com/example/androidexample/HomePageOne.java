package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePageOne extends AppCompatActivity {

    private Button homeBtn;
    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home1);

        messageText = findViewById(R.id.mainText);
        messageText.setText("Hello, welcome to home page 1");

        homeBtn = findViewById(R.id.homeButton);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageOne.this, HomePageTwo.class);
                startActivity(intent);
            }
        });

    }
}