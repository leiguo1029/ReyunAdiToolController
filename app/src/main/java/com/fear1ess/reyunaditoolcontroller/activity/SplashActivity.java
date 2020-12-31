package com.fear1ess.reyunaditoolcontroller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.fear1ess.reyunaditoolcontroller.SharedPreferenceUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPreferenceUtils.getServerConfig(0) != null) {
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, ServerConfigActivity.class));
        }

        finish();
    }
}