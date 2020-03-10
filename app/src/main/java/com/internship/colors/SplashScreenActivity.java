package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

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
}
