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

        Intent mainActivityIntent = new Intent(this, MainActivity.class);

        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {
                    startActivity(mainActivityIntent);
                    finish();
                }
            }
        };

        if (savedInstanceState == null) { //it doesn't work if thread was initially activated
            pauseAction(handler);
        }
    }

    private void pauseAction(Handler handler) {
        new HandlerThread("PAUSE") {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ignored) {

                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(THREAD_ACTIVATED, true);
    }
}
