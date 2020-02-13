package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.VideoView;

import com.badawy.carservice.R;

public class SplashScreenActivity extends AppCompatActivity {
//splash animation variables by AHMED TAREK...


    private VideoView videoView;
    private MediaPlayer mediaPlayer1;
    int currentvideoposition;
    //...............................................................





    /**
     *
     * Created  by Ahmed Tarek 15/11/2019
     * Modified by Mahmoud Badawy 16/11/2019
     * ...
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        hideSystemUI();
        setSplashScreen();



        //video view............... splash animation AHMED TAREK
        videoView=findViewById(R.id.splash_tv_video);

        //build the video uri
        Uri uri=Uri.parse("android.resource://" +getPackageName() +
                "/" +R.raw.v1);

        //attach or this step to link the new uri to our video view
        videoView.setVideoURI(uri);
        videoView.start();

//method to make our video run
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer1=mediaPlayer;

                mediaPlayer1.setLooping(true);


                if (currentvideoposition !=0){

                    mediaPlayer1.seekTo(currentvideoposition);
                    mediaPlayer1.start();
                }


            }
        });

    }


    private void setSplashScreen() {

        // Time in MilliSeconds 1000 = 1 second
        int SPLASH_TIME_OUT = 4000;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();


            }
        }, SPLASH_TIME_OUT);
    }

    private void hideSystemUI() {

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
