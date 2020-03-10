package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.concurrent.TimeUnit;


public class SplashScreenActivity extends AppCompatActivity {
    private final String THREAD_ACTIVATED = "thread_activated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState == null) {
            pauseAction();
        }
    }

    private void pauseAction() {
        Handler handler = new Handler();
        Runnable runnable = () -> {
            Intent mainActivityIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        };
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
