package com.example.activitytracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

public class Statistics extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView test;

    private Accelerometer accelerometer;
    private StepCounter stepCounter;
    private Gyroscope gyroscope;

    private int stepsReceived = 0;
    private int TotalSteps = 0;
    TextView txtSteps;
    TextView txtDistance;
    TextView txtCalories;
    Button btnStart;
    Button btnPause;
    private DecimalFormat decFormat;
    private Chronometer chrono;
    private Boolean running;
    private long pauseOffset;
    public GoogleApiClient mAPIclient;
    private String userWeight;
    private String userHeight;
    private long secondsPassed;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mAPIclient = new GoogleApiClient.Builder(Statistics.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(Statistics.this)
                .addOnConnectionFailedListener(Statistics.this)
                .build();
        mAPIclient.connect();

        decFormat = new DecimalFormat("#.##");
        userHeight = getIntent().getStringExtra("Height"); //gets the height of the user from previous page and sets it to testing
        userWeight = getIntent().getStringExtra("Weight");
        test = (TextView) findViewById(R.id.txtDistance);
        txtSteps = (TextView) findViewById(R.id.txtSteps);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtCalories = (TextView) findViewById(R.id.txtCalories);
        chrono = (Chronometer) findViewById(R.id.chrono);
        running = false;
        pauseOffset = 0;

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

                    stepCounter.setListener(new StepCounter.Listener() {
                        @Override
                        public void onStep(float step) {
                            Log.i(TAG, "onStep: " + step + " stepsReceived: " + stepsReceived + " totalSteps: " + TotalSteps);
                            if(stepsReceived < 1)
                            {
                                stepsReceived = (int)step;
                            }
                            else//shows the results
                            {
                                TotalSteps = (int)step - stepsReceived;
                                txtSteps.setText(String.valueOf(TotalSteps)); //displays steps
                                String height = getIntent().getStringExtra("Height");

                                int ht = Integer.parseInt(height);
                                double distance = (TotalSteps * ht * .415) / 63360;
                                txtDistance.setText(String.valueOf(decFormat.format(distance))); //displays distance
                                double adjustedWeight = Integer.valueOf(userWeight) / 2.2;

                                double stepsPerMile = 63360 / (ht * .415);
                                double metVal = 3.5;
                                double walkingSpeed = 3;

                                double caloriesBurned = Math.round(((adjustedWeight * metVal) / walkingSpeed) * (TotalSteps/stepsPerMile));
                                txtCalories.setText(String.valueOf(caloriesBurned));
                            }
                        }
                    });
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running) {
                    chrono.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chrono.getBase();
                    running = false;
                    secondsPassed = (SystemClock.elapsedRealtime() - chrono.getBase() / 1000);
                    double dSeconds = Double.valueOf(secondsPassed);
                }
                 TotalSteps = 0;
                 stepsReceived = 0;
                 txtSteps.setText(String.valueOf(0));
                 txtDistance.setText(String.valueOf(0)); //displays distance
                 txtCalories.setText(String.valueOf(0));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        stepCounter.register();
    }

    @Override
    public void onPause() {
        super.onPause();

        stepCounter.unregister();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(Statistics.this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(Statistics.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mAPIclient, 5000, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}