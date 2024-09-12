package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePageTwo extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private Button returnButton;     // define counter button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home2);             // link to Main activity XML

        messageText = findViewById(R.id.textBox1);
        messageText.setText("Hello, welcome to home page 2");

        returnButton = findViewById(R.id.homeButton);

        /* click listener on counter button pressed */
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(HomePageTwo.this, HomePageOne.class);
                startActivity(intent);
            }
        });
    }
}