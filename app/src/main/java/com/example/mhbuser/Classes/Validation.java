package com.example.mhbuser.Classes;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;


import com.santalu.maskedittext.MaskEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public boolean validateName(EditText etName, String sName) {
        if (TextUtils.isEmpty(sName)) {
            etName.requestFocus();
            return false;
        } else if (sName.length() < 3) {
            etName.setError("Name atleast contain 3 characters");
            etName.requestFocus();
            return false;
        }
        return true;
    }
    public boolean validateEmail(EditText etEmail, String sEmail) {

        if (TextUtils.isEmpty(sEmail)) {
            etEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }


    public boolean validatePhoneNo(MaskEditText metPhoneNo, String sPhoneNo) {
        if (TextUtils.isEmpty(sPhoneNo)) {
            metPhoneNo.requestFocus();
            return false;
        } else if (sPhoneNo.length() < 14) {
            metPhoneNo.setError("Please enter a valid phone no");
            metPhoneNo.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateCity(EditText etCity, String sCity) {
        if (TextUtils.isEmpty(sCity)) {
            etCity.requestFocus();
            return false;
        } else if (sCity.length() < 5) {
            etCity.setError("Please enter a valid city name");
            etCity.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateLocation(EditText etLocation, String sLocation) {
        if (TextUtils.isEmpty(sLocation)) {
            etLocation.requestFocus();
            return false;
        } else if (sLocation.length() < 5) {
            etLocation.setError("Please enter a valid country name");
            etLocation.requestFocus();
            return false;
        }
        return true;
    }


    public boolean ValidatePassword(TextView etPassword, String sPassword) {
        if (TextUtils.isEmpty(sPassword)) {
            etPassword.requestFocus();
            return false;
        } else if (sPassword.length() < 8) {
            etPassword.setError("Password min length 8");
            etPassword.requestFocus();
            return false;
        } else if (!isValidPassword(sPassword)) {
            etPassword.setError("Password must Contain small, capital, number");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        final String PASSWORD_PATTERN = "^(?=\\D*\\d)(?=[^a-z]*[a-z])(?=[^A-Z]*[A-Z])[\\w~@#$%^&*+=`|{}:;!.?\\\"\\s()\\[\\]-]{8,25}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean validateConfirmPassword(EditText etConfirmPassword, String sConfirmPassword, String sPassword) {
        if (TextUtils.isEmpty(sConfirmPassword)) {
            etConfirmPassword.requestFocus();
            return false;
        } else if (!sConfirmPassword.equals(sPassword)) {
            etConfirmPassword.setError("Password doesn't match");
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}