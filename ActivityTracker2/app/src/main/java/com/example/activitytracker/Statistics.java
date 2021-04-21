package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import static android.content.ContentValues.TAG;

public class Statistics extends AppCompatActivity {

    TextView test;

    private Accelerometer accelerometer;
    private StepCounter stepCounter;
    private Gyroscope gyroscope;

    private int stepsReceived = 0;
    private int TotalSteps = 0;
    TextView txtSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String testing = getIntent().getStringExtra("Height");
        test = (TextView) findViewById(R.id.txtDistance);
        test.setText(testing);
        txtSteps = (TextView) findViewById(R.id.txtSteps);

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        stepCounter = new StepCounter(this);

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
//                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                else if (ry < -1.0f)
                {
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
        });

        stepCounter.setListener(new StepCounter.Listener() {
            @Override
            public void onStep(float step) {
                Log.i(TAG, "onStep: " + step + "stepsReceived: " + stepsReceived + "totalSteps: " + TotalSteps);
//                getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                if(stepsReceived < 1)
                {
                    stepsReceived = (int)step;
                }
                else
                {
                    TotalSteps = (int)step - stepsReceived;
                    txtSteps.setText(String.valueOf(TotalSteps));
                }
            }
        });
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