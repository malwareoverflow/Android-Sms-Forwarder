package com.example.gm_pc.smsforwarder;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
public class WelcomeSplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash);
      //  Intent intent = new Intent(this, MainActivity.class);
      //  startActivity(intent);
     //   finish();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(WelcomeSplashActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
