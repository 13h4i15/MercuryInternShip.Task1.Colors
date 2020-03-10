package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Calendar;

public class SplashScreenActivity extends AppCompatActivity {
    private final String START_TIME_TAG = "millis";

    private Handler handler;
    private Runnable runnable;
    private long millisFromStart;
    private boolean visibilityFlag = true;
    private boolean delayEndedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState != null) {
            millisFromStart = savedInstanceState.getLong(START_TIME_TAG);
        } else {
            millisFromStart = Calendar.getInstance().getTimeInMillis();
        }

        long currentTime = Calendar.getInstance().getTimeInMillis();
        long millisOut = 5000 - (currentTime - millisFromStart);

        if (millisOut > 0) {
            pauseAction(millisOut);
        } else {
            startMainActivity();
        }
    }

    private void pauseAction(long timeOut) {
        handler = new Handler();
        runnable = () -> {
            if (visibilityFlag) startMainActivity();
            delayEndedFlag = true;
        };
        handler.postDelayed(runnable, timeOut);
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(START_TIME_TAG, millisFromStart);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibilityFlag = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        visibilityFlag = true;
        if (delayEndedFlag) startMainActivity();
    }
}