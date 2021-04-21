package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;
    Button btnSubmitHeight;
    TextView txtInputHeight;
    TextView txtTest;
    String inputHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtInputHeight = (TextView) findViewById(R.id.txtInputHeight);
        btnSubmitHeight = (Button) findViewById(R.id.btnSubmitHeight);
        txtTest = (TextView) findViewById((R.id.txtTesting));


        btnSubmitHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputHeight = txtInputHeight.getText().toString();
                txtTest.setText(inputHeight);
                openActivity2();
            }
        });


        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(tx > 1.0f)
                {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
                else if (ty < -1.0f)
                {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }
            }
        });


        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if(rx > 1.0f)
                {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                else if (ry < -1.0f)
                {
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
        });
        Log.d(TAG, "onCreate: Registered accelerometer listener");



    }
    public void openActivity2(){
        Intent intent = new Intent(this, Statistics.class);
        intent.putExtra("Height", inputHeight);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();

        accelerometer.register();
        gyroscope.register();
    }

    @Override
    public void onPause() {
        super.onPause();

        accelerometer.unregister();
        gyroscope.register();
    }

}