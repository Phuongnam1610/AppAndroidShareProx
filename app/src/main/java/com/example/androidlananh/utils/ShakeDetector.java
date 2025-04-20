package com.example.androidlananh.utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

public class ShakeDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_X = 10f;
    private static final int SHAKE_COUNT_THRESHOLD = 4;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int PAUSE_DURATION = 2000; // 2 seconds pause

    private OnShakeListener listener;
    private long lastShakeTime;
    private int shakeCount = 0;
    private long firstShakeTime;
    private boolean isPaused = false;

    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (listener != null && !isPaused) {
            float x = Math.abs(event.values[0]);

            if (x > SHAKE_THRESHOLD_X) {
                long currentTime = System.currentTimeMillis();

                if (shakeCount == 0) {
                    firstShakeTime = currentTime;
                    shakeCount = 1;
                } else {
                    if (currentTime - firstShakeTime < SHAKE_TIMEOUT) {
                        shakeCount++;
                        if (shakeCount >= SHAKE_COUNT_THRESHOLD) {
                            listener.onShake();
                            shakeCount = 0;
                            isPaused = true;

                            // Resume after 2 seconds
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isPaused = false;
                                }
                            }, PAUSE_DURATION);
                        }
                    } else {
                        shakeCount = 1;
                        firstShakeTime = currentTime;
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }
}