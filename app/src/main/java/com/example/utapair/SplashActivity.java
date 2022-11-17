package com.example.utapair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/* This file is about Splash */
public class SplashActivity extends AppCompatActivity {

    @Override
    /* this part will run when create this Activity */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* when Splash has finished showing, go to the main page */
        Intent iHome = new Intent(SplashActivity.this,MainActivity.class);

        /* give splash a delay of two seconds */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(iHome);
                finish();
            }
        }, 2000);
    }
}