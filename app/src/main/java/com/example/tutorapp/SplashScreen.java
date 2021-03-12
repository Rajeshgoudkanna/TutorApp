package com.example.tutorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation top_anim,btm_anim;
    ImageView imageView;
    TextView textlogo,texttagline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView= findViewById(R.id.imageView);
        textlogo= findViewById(R.id.textView4);
        texttagline=findViewById(R.id.textView5);
        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        btm_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        imageView.setAnimation(top_anim);
        textlogo.setAnimation(btm_anim);
        texttagline.setAnimation(btm_anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, login.class);
                startActivity(i);
            }
        },3000);
    }
}