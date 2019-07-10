package com.example.mhbuser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mhbuser.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toast.makeText(this, "Splash Screen", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LogInSignUp.class));
        finish();
    }
}
