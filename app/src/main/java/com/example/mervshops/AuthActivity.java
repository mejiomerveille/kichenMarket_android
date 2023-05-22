package com.example.mervshops;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.example.mervshops.Fragments.SignInFragment;
import com.example.namespace.R;


public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace((R.id.fragmentAuth),new SignInFragment()).commit();

    }
}