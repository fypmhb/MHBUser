package com.example.mhbuser.Activities;

import android.app.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mhbuser.Fragments.LogIn;
import com.example.mhbuser.R;

public class LogInSignUp extends AppCompatActivity {

    public  static Activity fin=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fin=this;
        setContentView(R.layout.activity_log_in_sign_up);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.LogInSignUp, new LogIn());
        ft.commit();
    }
}