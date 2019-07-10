package com.example.mhbuser.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.mhbuser.Activities.DashBoard;
import com.example.mhbuser.Activities.LogInSignUp;
import com.example.mhbuser.Classes.Validation;
import com.example.mhbuser.R;
import com.santalu.maskedittext.MaskEditText;

public class SignUp extends Fragment implements View.OnClickListener {

    public final static int IMAGE_REQUEST_CODE = 121;

    private Context context = null;
    private View fragment_view = null;
    //private SelectImagesAdapter selectImagesAdapter = null;
    private ImageView ivUserProfile = null,
            ivAddUserProfile = null;
    private Uri uriUserProfile = null;
    private EditText etUserFirstName = null,
            etUserLastName = null,
            etUserEmail = null,
            etUserCity = null,
            etUserLocation = null,
            etUserPassword = null,
            etUserConfirmPassword = null;
    private MaskEditText metUserPhoneNo = null;
    private Button btnUserSignUp = null;
    private TextView tvAlreadyHaveOne = null;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment_view = inflater.inflate(R.layout.sign_up, container, false);

        connectivity();

        //underline the already have one text view
        tvAlreadyHaveOne.setPaintFlags(tvAlreadyHaveOne.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ivAddUserProfile.setOnClickListener(this);
        btnUserSignUp.setOnClickListener(this);
        tvAlreadyHaveOne.setOnClickListener(this);

        return fragment_view;
    }

    private void connectivity() {
        context = getActivity();


       // selectImagesAdapter = new SelectImagesAdapter(context);

        ivUserProfile = (ImageView) fragment_view.findViewById(R.id.iv_user_profile);
        ivAddUserProfile = (ImageView) fragment_view.findViewById(R.id.iv_add_user_profile);
        etUserFirstName = (EditText) fragment_view.findViewById(R.id.et_first_name);
        etUserLastName = (EditText) fragment_view.findViewById(R.id.et_last_name);
        etUserEmail = (EditText) fragment_view.findViewById(R.id.et_user_email);
        metUserPhoneNo = (MaskEditText) fragment_view.findViewById(R.id.met_phone);
        etUserCity = (EditText) fragment_view.findViewById(R.id.et_city);
        etUserLocation = (EditText) fragment_view.findViewById(R.id.et_location);
        etUserPassword = (EditText) fragment_view.findViewById(R.id.et_user_password);
        etUserConfirmPassword = (EditText) fragment_view.findViewById(R.id.et_confirm_password);
        tvAlreadyHaveOne = (TextView) fragment_view.findViewById(R.id.tv_already_have_one);
        btnUserSignUp=(Button)fragment_view.findViewById(R.id.btn_sign_up);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_add_user_profile) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,
                    "Select Profile Image"), IMAGE_REQUEST_CODE);
        } else if (v.getId() == R.id.btn_sign_up) {
            signUp();
        } else if (v.getId() == R.id.tv_already_have_one) {
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.LogInSignUp, new LogIn());
            ft.commit();
        }
    }

    private void signUp() {

        String sUserFirstName = etUserFirstName.getText().toString().trim();
        String sUserLastName = etUserLastName.getText().toString().trim();
        String sUserEmail = etUserEmail.getText().toString().trim();
        String sUserPhoneNo = metUserPhoneNo.getText().toString().trim();
        String sUserCity = etUserCity.getText().toString().trim();
        String sUserLocation = etUserLocation.getText().toString().trim();
        String sPassword = etUserPassword.getText().toString().trim();
        String sConfirmPassword = etUserConfirmPassword.getText().toString().trim();


        Validation signUpValidation = new Validation();

        if (!signUpValidation.validateName(etUserFirstName, sUserFirstName)) {
            etUserFirstName.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserFirstName.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateName(etUserLastName, sUserLastName)) {
            etUserLastName.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserLastName.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (uriUserProfile == null) {
            Toast.makeText(context, "Please select User Profile Picture", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!signUpValidation.validateEmail(etUserEmail, sUserEmail,"validation")) {
            etUserEmail.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserEmail.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validatePhoneNo(metUserPhoneNo, sUserPhoneNo)) {
            metUserPhoneNo.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            metUserPhoneNo.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateCity(etUserCity, sUserCity)) {
            etUserCity.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserCity.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateLocation(etUserLocation, sUserLocation)) {
            etUserLocation.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserLocation.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.ValidatePassword(etUserPassword, sPassword)) {
            etUserPassword.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserPassword.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateConfirmPassword(etUserConfirmPassword, sConfirmPassword, sPassword)) {
            etUserConfirmPassword.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return;
        } else
            etUserConfirmPassword.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        Intent i=new Intent(getContext(), DashBoard.class);
        startActivity(i);
        LogInSignUp.fin.finish();
    }

    //Select image and set to imageView

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            uriUserProfile = data.getData();

            try {
                //getting selected image into bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriUserProfile);
                //setting bitmap into imageview
                ivUserProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}