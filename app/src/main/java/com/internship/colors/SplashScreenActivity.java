package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashScreenActivity extends AppCompatActivity {
    private final String START_TIME_TAG = "millis";

    private long millisFromStart;
    private CompositeDisposable compositeDisposable;
    private boolean visibilityFlag = true;

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
        long millisOut = 2000 - (currentTime - millisFromStart);

        if (millisOut > 0) {
            compositeDisposable = new CompositeDisposable();
            Disposable disposable = Observable.timer(millisOut, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(v -> {
                        if (visibilityFlag) startMainActivity();
                        compositeDisposable.dispose();
                    });
            compositeDisposable.add(disposable);
        } else {
            startMainActivity();
        }
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
        if (compositeDisposable != null) compositeDisposable.dispose();
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
        if (compositeDisposable.isDisposed()) startMainActivity();
    }
}
