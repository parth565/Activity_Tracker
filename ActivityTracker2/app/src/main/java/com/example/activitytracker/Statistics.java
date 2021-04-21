package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String testing = getIntent().getStringExtra("Height");
        test = (TextView) findViewById(R.id.txtDistance);
        test.setText(testing);


    }
}