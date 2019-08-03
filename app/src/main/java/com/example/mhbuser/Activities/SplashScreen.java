package com.example.mhbuser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mhbuser.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {


    private FirebaseAuth mAuth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        connectivity();

        checkFireBaseState();

    }

    private void connectivity() {

        mAuth = FirebaseAuth.getInstance();
    }

    private void checkFireBaseState() {
        Toast.makeText(this, "Splash Screen", Toast.LENGTH_SHORT).show();
        //if user already login
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        } else {
            //if user not login
            startActivity(new Intent(getApplicationContext(), LogInSignUp.class));
            finish();
        }
    }

}

