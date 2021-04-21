package com.example.activitytracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetector {
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
    private Sensor stepDetector;
    private SensorEventListener sensorEventListener;

    StepDetector(Context context)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
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
        sensorManager.registerListener(sensorEventListener, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void unregister()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
