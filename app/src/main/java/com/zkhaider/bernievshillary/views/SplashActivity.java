package com.zkhaider.bernievshillary.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zkhaider.bernievshillary.R;

/**
 * Created by ZkHaider on 5/29/16.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        int splashDisplayLength = 800;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMainActivity();
            }
        }, splashDisplayLength);

    }

    private void goToMainActivity() {



        Intent openMainActivity = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(openMainActivity);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}
