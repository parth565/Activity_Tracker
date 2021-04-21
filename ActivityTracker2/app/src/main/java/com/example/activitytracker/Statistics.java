package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

public class Statistics extends AppCompatActivity {

    TextView test;

    private Accelerometer accelerometer;
    private StepCounter stepCounter;
    private Gyroscope gyroscope;

    private int stepsReceived = 0;
    private int TotalSteps = 0;
    TextView txtSteps;
    TextView txtDistance;
    Button btnStart;
    Button btnPause;
    private DecimalFormat decFormat;
    private Chronometer chrono;
    private Boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        decFormat = new DecimalFormat("#.##");
        String testing = getIntent().getStringExtra("Height"); //gets the height of the user from previous page and sets it to testing
        test = (TextView) findViewById(R.id.txtDistance);
        test.setText(testing);
        txtSteps = (TextView) findViewById(R.id.txtSteps);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        chrono = (Chronometer) findViewById(R.id.chrono);
        running = false;
        pauseOffset = 0;

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        stepCounter = new StepCounter(this);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnPause = (Button) findViewById(R.id.btnPause);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!running){
                    chrono.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chrono.start();
                    running = true;
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    chrono.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chrono.getBase();
                    running = false;
                }
            }
        });


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

/*
        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if(rx > 1.0f)
                {
//                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                else if (ry < -1.0f)
                {
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
        });*/

        stepCounter.setListener(new StepCounter.Listener() {
            @Override
            public void onStep(float step) {
                Log.i(TAG, "onStep: " + step + "stepsReceived: " + stepsReceived + "totalSteps: " + TotalSteps);
//                getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                if(stepsReceived < 1)
                {
                    stepsReceived = (int)step;
                }
                else//shows the results
                {
                    TotalSteps = (int)step - stepsReceived;
                    txtSteps.setText(String.valueOf(TotalSteps));
                    String height = getIntent().getStringExtra("Height");
                    int ht = Integer.parseInt(height);
                    double distance = (TotalSteps * ht * .415) / 63360;
                    txtDistance.setText(String.valueOf(decFormat.format(distance)));

                }
            }
        });
    }

    public void startChrono(View v){

    }
    public void pauseChrono(View v){

    }
    public void resetChrono(View v){
        chrono.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(this, "registering!", Toast.LENGTH_SHORT).show();
        accelerometer.register();
        gyroscope.register();
        stepCounter.register();
    }

    @Override
    public void onPause() {
        super.onPause();

        accelerometer.unregister();
        gyroscope.unregister();
        stepCounter.unregister();
    }
}