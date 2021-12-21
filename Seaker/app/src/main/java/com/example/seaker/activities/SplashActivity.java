package com.example.seaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.seaker.R;

public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler();

    private static int TIME_OUT = 2*1000; //Time to launch the another activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, ChooseRoleActivity.class);
                startActivity(intent);
            }
        }, TIME_OUT);
    }
}