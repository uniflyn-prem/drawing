package com.example.canvasdrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;

        if (getSupportActionBar()!=null){

            getSupportActionBar().hide();

        }

        init();
    }

    public void init() {


        new Handler().postDelayed(() -> {
            Intent intent =  new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        }, 3000);
    }

}

