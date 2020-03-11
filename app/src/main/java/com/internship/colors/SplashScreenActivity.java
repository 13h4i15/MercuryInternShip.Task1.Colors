package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Calendar;

public class SplashScreenActivity extends AppCompatActivity {
    private final static String START_TIME_TAG = "millis";

    private Handler handler;
    private Runnable startMainRunnable;
    private long millisAtStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState != null) {
            millisAtStart = savedInstanceState.getLong(START_TIME_TAG);
        } else {
            millisAtStart = Calendar.getInstance().getTimeInMillis();
        }

    }

    private void pauseAction(long timeOut) {
        handler = new Handler();
        startMainRunnable = this::startMainActivity;
        handler.postDelayed(startMainRunnable, timeOut);
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(START_TIME_TAG, millisAtStart);
    }

    @Override
    protected void onPause() {
        if (handler != null) handler.removeCallbacks(startMainRunnable);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        long millisOut = 2000 - (currentTime - millisAtStart);
        if (millisOut > 0) {
            pauseAction(millisOut);
        } else {
            startMainActivity();
        }
    }
}