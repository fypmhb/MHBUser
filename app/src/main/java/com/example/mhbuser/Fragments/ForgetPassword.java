package com.example.mhbuser.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mhbuser.Classes.Validation;
import com.example.mhbuser.R;

public class ForgetPassword extends Fragment implements View.OnClickListener {
    private View fragment_view = null;
    private Context context = null;
    private EditText etResetEmail = null;
    private Button btnResetPassword = null,
            btnBack = null;
    private Validation loginValidation = null;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.forget_password, container, false);

        connectivity();

        getDataFromLogIn();

        //setting click listeners on buttons
        btnResetPassword.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        return fragment_view;
    }

    private void getDataFromLogIn() {
        Bundle email = getArguments();
        assert email != null;
        if (email != null)
            etResetEmail.setText(email.getString("email"));
    }

    private void connectivity() {
        context = getActivity();

        etResetEmail = (EditText) fragment_view.findViewById(R.id.et_reset_email);
        btnResetPassword = (Button) fragment_view.findViewById(R.id.btn_reset_password);
        btnBack = (Button) fragment_view.findViewById(R.id.btn_back);
        loginValidation = new Validation();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset_password) {
            String sEmail = etResetEmail.getText().toString().trim();

            if (!loginValidation.validateEmail(etResetEmail, sEmail))
                return;

            replaceFragment();

            Toast.makeText(context, "Password is reset", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_back) {
            replaceFragment();
        }
    }

    private void replaceFragment() {
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LogIn passEmail = new LogIn();
        if (loginValidation.validateEmail(etResetEmail, etResetEmail.getText().toString().trim()))
        {
            Bundle email = new Bundle();
            email.putString("email", etResetEmail.getText().toString().trim());
            passEmail.setArguments(email);
        }
        ft.replace(R.id.LogInSignUp, passEmail);
        ft.commit();
    }
}