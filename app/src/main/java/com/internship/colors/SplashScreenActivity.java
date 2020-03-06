package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SplashScreenActivity extends AppCompatActivity {
    private final String THREAD_ACTIVATED = "thread_activated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent mainActivityIntent = new Intent(this, MainActivity.class);

        if (savedInstanceState == null) { //it doesn't work if thread was initially activated
            Observable.fromCallable(new CallablePauseAction())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(v -> {
                        startActivity(mainActivityIntent);
                        finish();
                    });
        }
    }

    private boolean pauseAction() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }
        return true;
    }

    private class CallablePauseAction implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            return pauseAction();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(THREAD_ACTIVATED, true);
    }
}
