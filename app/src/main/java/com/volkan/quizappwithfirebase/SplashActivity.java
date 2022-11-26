package com.volkan.quizappwithfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    int timer = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer--;
                progressBar.setProgress(timer);
            }

            @Override
            public void onFinish() {
                finish();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }.start();

    }
}