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
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;

    Button btnSubmitHeight;
    TextView txtInputHeight;
    TextView txtTest;
    String inputHeight;

    private Sensor StepCounter;
    private boolean isStepCounterPresent;
//    private Accelerometer accelerometer;
//    private StepCounter stepCounter;
//    private StepDetector stepDetector;
//    private Gyroscope gyroscope;

    private TextView textViewStepCounter, textViewStepDetector;
    int stepCount = 0;
//    private int stepsReceived = 0;
//    private int TotalSteps = 0;
//    private int stepDet = 0;



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



//        Log.d(TAG, "onCreate: Initializing Sensor Services");
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        accelerometer = new Accelerometer(this);
//        gyroscope = new Gyroscope(this);
//        accelerometer.setListener(new Accelerometer.Listener() {
//            @Override
//            public void onTranslation(float tx, float ty, float tz) {
//                if(tx > 1.0f)
//                {
//                    getWindow().getDecorView().setBackgroundColor(Color.RED);
//                }
//                else if (ty < -1.0f)
//                {
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                }
//            }
//        });
//
//        gyroscope.setListener(new Gyroscope.Listener() {
//            @Override
//            public void onRotation(float rx, float ry, float rz) {
//                if(rx > 1.0f)
//                {
//                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
//                }
//                else if (ry < -1.0f)
//                {
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                }
//            }
//        });
//        Log.d(TAG, "onCreate: Registered accelerometer listener");
//

        textViewStepCounter = findViewById(R.id.textViewStepCounter);
//        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            StepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isStepCounterPresent = true;
        } else {
            textViewStepCounter.setText("Counter sensor is not present");
            isStepCounterPresent = false;
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Toast.makeText(this, "Sensor accessed", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onSensorChanged: Yes");
        if(event.sensor == StepCounter){
            stepCount = (int) event.values[0];
            textViewStepCounter.setText(String.valueOf(stepCount));
        }
    }
    public void openActivity2(){
        Intent intent = new Intent(this, Statistics.class);
        intent.putExtra("Height", inputHeight);
        startActivity(intent);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

//        accelerometer = new Accelerometer(this);
//        gyroscope = new Gyroscope(this);
//        stepCounter = new StepCounter(this);
//        stepDetector = new StepDetector(this);
//
//        accelerometer.setListener(new Accelerometer.Listener() {
//            @Override
//            public void onTranslation(float tx, float ty, float tz) {
//                if(tx > 1.0f)
//                {
////                    getWindow().getDecorView().setBackgroundColor(Color.RED);
//                }
//                else if (ty < -1.0f)
//                {
////                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//                }
//            }
//        });
//
//        gyroscope.setListener(new Gyroscope.Listener() {
//            @Override
//            public void onRotation(float rx, float ry, float rz) {
//                if(rx > 1.0f)
//                {
////                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
//                }
//                else if (ry < -1.0f)
//                {
////                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                }
//            }
//        });
//
//        stepCounter.setListener(new StepCounter.Listener() {
//            @Override
//            public void onStep(float step) {
//                Log.i(TAG, "onStep: " + step + "stepsReceived: " + stepsReceived + "totalSteps: " + TotalSteps);
//                getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//                if(stepsReceived < 1)
//                {
//                    stepsReceived = (int)step;
//                }
//                else
//                {
//                    TotalSteps = (int)step - stepsReceived;
//                    Log.i(TAG, "onStep: " + step + "stepsReceived: " + stepsReceived + "totalSteps: " + TotalSteps);
//                }
//            }
//        });
//
//        Log.d(TAG, "onCreate: Registered accelerometer listener");
//    }

    @Override
    public void onResume() {
        super.onResume();

//        accelerometer.register();
//        gyroscope.register();
//        stepCounter.register();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, StepCounter, SensorManager.SENSOR_DELAY_FASTEST);
            Toast.makeText(this, "Sensor registered", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//        accelerometer.unregister();
//        gyroscope.unregister();
//        stepCounter.unregister();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
            sensorManager.unregisterListener(this, StepCounter);
    }

}