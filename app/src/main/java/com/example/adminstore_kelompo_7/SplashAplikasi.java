package com.example.adminstore_kelompo_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashAplikasi extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_aplikasi);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn()) {
                    Intent intent = new Intent(SplashAplikasi.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Jika belum login, pergi ke ActivityLogin
                    Intent intent = new Intent(SplashAplikasi.this, LoginAdmin.class);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}