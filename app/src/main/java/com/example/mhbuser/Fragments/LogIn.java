package com.example.mhbuser.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mhbuser.Activities.DashBoard;
import com.example.mhbuser.Activities.LogInSignUp;
import com.example.mhbuser.Classes.CNetworkConnection;
import com.example.mhbuser.Classes.Validation;
import com.example.mhbuser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends Fragment implements View.OnClickListener {

    private ProgressDialog progressDialog = null;

    private View fragment_view = null;
    private Context context = null;
    private Button btnLogin = null;
    private TextView tvForgetPassword = null,
            tvCreateOne = null;
    private EditText etEmail = null,
            etPassword = null;
    private String sEmail=null,
                    sPassword=null;

    Validation loginValidation = null;
    private RelativeLayout rlCloseKeyboard = null;
    private ToggleButton tbShowHidePassword = null;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.log_in, container, false);


        connectivity();

        getDataFromForgetPassword();

        //underline the create one text view
        tvCreateOne.setPaintFlags(tvCreateOne.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //setting click listeners on buttons
        rlCloseKeyboard.setOnClickListener(this);
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

        rlCloseKeyboard = (RelativeLayout) fragment_view.findViewById(R.id.rl_hide_soft_keyboard);

        etEmail = (EditText) fragment_view.findViewById(R.id.et_email);
        etPassword = (EditText) fragment_view.findViewById(R.id.et_password);
        tbShowHidePassword = (ToggleButton) fragment_view.findViewById(R.id.tb_show_hide_password);

        btnLogin = (Button) fragment_view.findViewById(R.id.btn_login);
        tvForgetPassword = (TextView) fragment_view.findViewById(R.id.tv_forget_password);
        tvCreateOne = (TextView) fragment_view.findViewById(R.id.tv_create_one);
        loginValidation = new Validation();
        progressDialog = new ProgressDialog(context);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.rl_hide_soft_keyboard) {
            hideSoftKeyboard(v);
        }
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
            if (loginValidation.validateEmail(etEmail, etEmail.getText().toString().trim()))
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

    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void logIn() {
         sEmail = etEmail.getText().toString().trim();
         sPassword = etPassword.getText().toString().trim();


        if (!allValidation())
            return;

        //check Internet Connection
        if (!checkInternetConnection()) {
            return;
        }

        logInUserToFireBase();


    }

    private boolean checkInternetConnection() {
        // if there is no internet connection
        CNetworkConnection CNetworkConnection = new CNetworkConnection();
        if (CNetworkConnection.isConnected(context)) {
            CNetworkConnection.buildDialog(context).show();
            return false;
        }
        return true;
    }

    private void logInUserToFireBase() {

        progressDialog.setMessage("LogIn...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        Intent i=new Intent(getContext(), DashBoard.class);
                        startActivity(i);
                        LogInSignUp.fin.finish();
                        Toast.makeText(context, "Logion", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean allValidation() {

        if (!loginValidation.validateEmail(etEmail, sEmail)) {
            etEmail.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etEmail.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!loginValidation.ValidatePassword(etPassword, sPassword)) {
            etPassword.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etPassword.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        return true;
    }
}
