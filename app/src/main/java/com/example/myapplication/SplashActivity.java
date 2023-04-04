package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
private TextView UEFA;
    private MediaPlayer mediaPlayer;
private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mediaPlayer = MediaPlayer.create(this, R.raw.uefasound);
        UEFA= findViewById(R.id.uefa);
        image= findViewById(R.id.image);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        anim.setFillAfter(true);
        UEFA.setAnimation(anim);
        image.setAnimation(anim);
        mediaPlayer.start();
        new Thread(){

            public void run(){
                try{
                    sleep(16000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Intent intent =new Intent(SplashActivity.this,AuthenticationActivity.class);
                startActivity(intent);
            }
        }.start();
    }}

