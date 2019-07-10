package com.example.mhbuser.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mhbuser.Activities.DashBoard;
import com.example.mhbuser.Activities.LogInSignUp;
import com.example.mhbuser.Classes.Validation;
import com.example.mhbuser.R;

public class LogIn extends Fragment implements View.OnClickListener {

    private View fragment_view = null;
    private Context context = null;
    private Button btnLogin = null;
    private TextView tvForgetPassword = null,
            tvCreateOne = null;
    private EditText etEmail = null,
            etPassword = null;
    Validation loginValidation = null;
    private ToggleButton tbShowHidePassword = null;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.log_in, container, false);


        connectivity();

        getDataFromForgetPassword();

        //underline the create one text view
        tvCreateOne.setPaintFlags(tvCreateOne.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //setting click listeners on buttons
        tbShowHidePassword.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvCreateOne.setOnClickListener(this);

        return fragment_view;
    }

    private void getDataFromForgetPassword() {
        Bundle email = getArguments();
        assert email != null;
        if (email != null)
            etEmail.setText(email.getString("email"));
    }

    private void connectivity() {
        context = getActivity();

        etEmail = (EditText) fragment_view.findViewById(R.id.et_email);
        etPassword = (EditText) fragment_view.findViewById(R.id.et_password);
        tbShowHidePassword = (ToggleButton) fragment_view.findViewById(R.id.tb_show_hide_password);

        btnLogin = (Button) fragment_view.findViewById(R.id.btn_login);
        tvForgetPassword = (TextView) fragment_view.findViewById(R.id.tv_forget_password);
        tvCreateOne = (TextView) fragment_view.findViewById(R.id.tv_create_one);
        loginValidation = new Validation();
    }

    @Override
    public void onClick(View v) {
        //hide or show password
        if (v.getId() == R.id.tb_show_hide_password) {
            if (tbShowHidePassword.isChecked()) {
                tbShowHidePassword.setChecked(true);
                tbShowHidePassword.setBackgroundResource(R.drawable.ic_hide_password);
                etPassword.setTransformationMethod(null);
            } else {
                tbShowHidePassword.setChecked(false);
                tbShowHidePassword.setBackgroundResource(R.drawable.ic_show_password);
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            //login setup
        } else if (v.getId() == R.id.btn_login) {
            logIn();
        } else if (v.getId() == R.id.tv_forget_password) {
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ForgetPassword passEmail = new ForgetPassword();
            if (loginValidation.validateEmail(etEmail, etEmail.getText().toString().trim(),"corectness"))
            {
                Bundle email = new Bundle();
                email.putString("email", etEmail.getText().toString().trim());
                passEmail.setArguments(email);
            }
            ft.replace(R.id.LogInSignUp, passEmail);
            ft.commit();
        } else if (v.getId() == R.id.tv_create_one) {
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.LogInSignUp, new SignUp());
            ft.commit();
        }
    }

    private void logIn() {
        String sEmail = etEmail.getText().toString().trim();
        String sPassword = etPassword.getText().toString().trim();

        if (!loginValidation.validateEmail(etEmail, sEmail,"validation")) {
            etEmail.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        }
        if (!loginValidation.ValidatePassword(etPassword, sPassword)) {
            etPassword.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        }

        Intent i=new Intent(getContext(), DashBoard.class);
        startActivity(i);
        LogInSignUp.fin.finish();
        Toast.makeText(context, "Logion", Toast.LENGTH_SHORT).show();
    }
}