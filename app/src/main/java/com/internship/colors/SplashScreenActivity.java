package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashScreenActivity extends AppCompatActivity {
    private final static String START_TIME_TAG = "millis";

    private long millisAtStart;
    private Disposable disposable;

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(START_TIME_TAG, millisAtStart);
    }


    @Override
    protected void onPause() {
        if (disposable != null) disposable.dispose();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        long currentTime = Calendar.getInstance().getTimeInMillis();
        long millisOut = 2000 - (currentTime - millisAtStart);

        if (millisOut > 0) {
            disposable = Single.timer(millisOut, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(v -> startMainActivity());
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}
