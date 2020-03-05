package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.concurrent.TimeUnit;


public class PreviewScreenActivity extends AppCompatActivity{
    private final String THREADS_COUNT = "count";

    int threadsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);
        if(savedInstanceState != null){
            threadsCount = savedInstanceState.getInt(THREADS_COUNT);
        }

        Intent mainActivityIntent = new Intent(this, MainActivity.class);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
               if(msg.what == 0){
                   startActivity(mainActivityIntent);
                   finish();
               }
            }
        };

        if(threadsCount == 0){
            threadsCount++;
            pauseAction(handler);
        }
    }

    private void pauseAction(Handler handler){
        new HandlerThread("PAUSE"){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                }catch (InterruptedException ignored){

                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(THREADS_COUNT, threadsCount);
    }
}
