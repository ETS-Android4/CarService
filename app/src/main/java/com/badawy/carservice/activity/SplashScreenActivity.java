package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.badawy.carservice.R;

public class SplashScreenActivity extends AppCompatActivity {
    private  static int SPLASH_TIME_OUT=10000;//DATA MEM MEANS 2SEC m

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);//error is here
                startActivity(i);
                finish();


            }
        },SPLASH_TIME_OUT);
    }
}
