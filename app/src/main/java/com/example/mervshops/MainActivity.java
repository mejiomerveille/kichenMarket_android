package com.example.mervshops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.namespace.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

//this code vill passe the app for 1.5
        Handler handler =new Handler();
        handler.postDelayed(() -> {
            SharedPreferences userpref=getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            boolean isloggedIn=userpref.getBoolean("isloggedIn",false);
            if(isloggedIn){
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            }
            else {
                isFirstTime();
            }
        },1500);
    }
    private void isFirstTime() {
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("onboard",MODE_PRIVATE);
        boolean isFirstTime=preferences.getBoolean("isFirstTime",true);
        if(isFirstTime){
            //changed it false
            SharedPreferences.Editor editor= preferences.edit();
            editor.putBoolean("isFirstTime",false);
            editor.apply();

            //star onboard activity
            startActivity(new Intent(MainActivity.this,AuthActivity.class));
            finish();
        }else{
            //star Authactivity
            startActivity(new Intent(MainActivity.this,OnboardActivity.class));
            finish();
        }
    }
}