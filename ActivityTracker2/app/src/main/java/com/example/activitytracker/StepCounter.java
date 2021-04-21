package com.example.activitytracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class StepCounter {
    public interface Listener
    {
        void onStep(float step);
    }

    private Listener listener;
    public void setListener(Listener a)
    {
        listener = a;
    }

    private SensorManager sensorManager;
    private Sensor stepCounter;
    private Sensor stepDetector;
    private SensorEventListener sensorEventListener;


    StepCounter(Context context)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.i(TAG, "onSensorChanged: Yes");
                if(listener != null)
                {
                    listener.onStep(event.values[0]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
    }

    public void register()
    {
        sensorManager.registerListener(sensorEventListener, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregister()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
