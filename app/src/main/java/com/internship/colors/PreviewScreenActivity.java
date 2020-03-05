package com.internship.colors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class PreviewScreenActivity extends AppCompatActivity{
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                startActivity(mainActivityIntent);
                finish();
            }
        };
        pauseAction();
    }


    private void pauseAction(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    handler.sendEmptyMessage(0);
                }catch (InterruptedException e){

                }
            }
        });
        thread.start();
    }
}
