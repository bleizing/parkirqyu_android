package com.bleizing.parkirqyu.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bleizing.parkirqyu.R;
import com.bleizing.parkirqyu.models.Model;
import com.bleizing.parkirqyu.models.User;
import com.bleizing.parkirqyu.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        intent = new Intent(SplashActivity.this, LoginActivity.class);

        PrefUtils prefUtils = new PrefUtils(this);

        if (prefUtils.isLoggedIn()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);

            User user = prefUtils.getUser();
            Model.setUser(user);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
