package com.example.activitytracker;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService(){
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name){
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivity(result.getProbableActivities());
        }
    }

    private void handleDetectedActivity(List<DetectedActivity> probableActivities){
        for(DetectedActivity activity : probableActivities ){
            switch(activity.getType()){
                case DetectedActivity.STILL:{
                    Log.d(TAG, "handleDetectedActivity: STILL: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.RUNNING:{
                    Log.d(TAG, "handleDetectedActivity: RUNNING: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.WALKING:{
                    Log.d(TAG, "handleDetectedActivity: WALKING: " + activity.getConfidence());
                    break;
                }
            }

        }
    }

}
