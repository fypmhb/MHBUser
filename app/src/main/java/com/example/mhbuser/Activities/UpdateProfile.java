package com.example.mhbuser.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.mhbuser.Classes.*;
import com.example.mhbuser.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santalu.maskedittext.MaskEditText;

import java.io.ByteArrayOutputStream;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {

    public final static int IMAGE_REQUEST_CODE = 121;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=120;

    private Context context = null;
    private RelativeLayout rlCloseKeyboard = null;
    private ImageView ivUserProfile = null,
            ivAddUserProfile = null;
    private String sUserProfileName = null,
            sUserProfileDownloadUri = null;
    private Uri uriUserProfile = null;
    private EditText etUserFirstName = null,
            etUserLastName = null,
            etUserEmail = null,
            etUserCity = null,
            etUserCountry=null,
            etUserLocation = null;
    private String sUserFirstName = null,
            sUserLastName = null,
            sUserEmail = null,
            sUserPhoneNo = null,
            sUserCity = null,
            sUserCountry=null,
            sUserLocation = null;
    private MaskEditText metUserPhoneNo = null;
    private Button btnUpdate = null;
    private  CSignUpData cSignUpData=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        connectivity();
        getDataFromFireBase();


        rlCloseKeyboard.setOnClickListener(this);
        ivAddUserProfile.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);



    }


    private void connectivity() {
        context = this;


        rlCloseKeyboard = (RelativeLayout) findViewById(R.id.rl_hide_soft_keyboard);
        ivUserProfile = (ImageView) findViewById(R.id.iv_user_profile);
        ivAddUserProfile = (ImageView)findViewById(R.id.iv_add_user_profile);
        etUserFirstName = (EditText) findViewById(R.id.et_first_name);
        etUserLastName = (EditText) findViewById(R.id.et_last_name);
        etUserEmail = (EditText) findViewById(R.id.et_user_email);
        metUserPhoneNo = (MaskEditText)findViewById(R.id.met_phone);
        etUserCity = (EditText)findViewById(R.id.et_city);
        etUserCountry=(EditText)findViewById(R.id.et_country);
        etUserLocation = (EditText)findViewById(R.id.et_location);
        btnUpdate = (Button)findViewById(R.id.btn_update);
    }

    private void getDataFromFireBase() {
                FirebaseFirestore.getInstance()
                .collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            cSignUpData = documentSnapshot.toObject(CSignUpData.class);
                            showDataOnView();
                        }
                    }
                });
    }

    private void showDataOnView() {

        etUserFirstName.setText(cSignUpData.getsUserFirstName());
        etUserLastName.setText(cSignUpData.getsUserLastName());
        etUserEmail.setText(cSignUpData.getsEmail());
        metUserPhoneNo.setText(cSignUpData.getsPhoneNo());
        etUserCity.setText(cSignUpData.getsCity());
        etUserCountry.setText(cSignUpData.getsCountry());
        etUserLocation.setText(cSignUpData.getsLocation());


        Glide.with(getApplicationContext())
                .load(cSignUpData.getsUserProfileImageUri())
                .into(ivUserProfile);

        uriUserProfile = Uri.parse(cSignUpData.getsUserProfileImageUri());
        sUserProfileName= cSignUpData.getsUserProfileImageUri();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_hide_soft_keyboard) {
            hideSoftKeyboard(v);
        } else if (v.getId() == R.id.iv_add_user_profile) {
            profilePicture();
        } else if (v.getId() == R.id.btn_update) {
            signUp();
        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void profilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Complete This Action Using");
        builder.setTitle("Please Confirm");
        builder.setCancelable(false);
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //pass false for open gallery to select one picture.
                intentForOpenGallery();
            }
        }).setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intentForOpenCamera();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog1 = builder.create();
        dialog1.show();
    }

    private void intentForOpenCamera() {
        ask_CheckCameraStoragePermission_OpenCamera();
    }

    private void ask_CheckCameraStoragePermission_OpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String permissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, IMAGE_REQUEST_CODE);
            } else { // permission granted
                openCamera();
            }
        } else {
//            < M
            openCamera();
        }
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, IMAGE_REQUEST_CODE);
    }

    private void intentForOpenGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                "Select Profile Image"), CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    private Uri saveInGallery(Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }


    private void signUp() {

        getDataFromView();

        if (!allValidation())
            return;
        //check Internet Connection
        if (!checkInternetConnection()) {
            return;
        }

        //FireBase work

        CSignUpData cSignUpDataNew=new CSignUpData(sUserFirstName,sUserLastName,sUserEmail,sUserPhoneNo,sUserCity,sUserCountry,sUserLocation);

        //FireBase work

        if(uriUserProfile.equals(Uri.parse(cSignUpData.getsUserProfileImageUri())))
        {
            cSignUpDataNew.setsUserProfileImageUri(cSignUpData.getsUserProfileImageUri());
            new CUploadUserDataToFireBase(context,cSignUpDataNew);

        }
        else
        {
            new CUploadUserDataToFireBase(context, uriUserProfile,sUserProfileName,Uri.parse(cSignUpData.getsUserProfileImageUri()),cSignUpDataNew);
        }


    }

    private void getDataFromView() {
        sUserFirstName = etUserFirstName.getText().toString().trim();
        sUserLastName = etUserLastName.getText().toString().trim();
        sUserEmail = etUserEmail.getText().toString().trim();
        sUserPhoneNo = metUserPhoneNo.getText().toString().trim();
        sUserCity = etUserCity.getText().toString().trim();
        sUserCountry=etUserCountry.getText().toString().trim();
        sUserLocation = etUserLocation.getText().toString().trim();


    }

    private Boolean allValidation() {

        Validation signUpValidation = new Validation();

        if (!signUpValidation.validateName(etUserFirstName, sUserFirstName)) {
            etUserFirstName.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etUserFirstName.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateName(etUserLastName, sUserLastName)) {
            etUserLastName.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etUserLastName.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (uriUserProfile == null) {
            Toast.makeText(context, "Please select User Profile Picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!signUpValidation.validateEmail(etUserEmail, sUserEmail)) {
            etUserEmail.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etUserEmail.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validatePhoneNo(metUserPhoneNo, sUserPhoneNo)) {
            metUserPhoneNo.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            metUserPhoneNo.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateCity(etUserCity, sUserCity)) {
            etUserCity.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etUserCity.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateCity(etUserCountry, sUserCountry)) {
            etUserCountry.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etUserCountry.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        if (!signUpValidation.validateLocation(etUserLocation, sUserLocation)) {
            etUserLocation.setBackground(context.getResources().getDrawable(R.drawable.round_red));
            return false;
        } else
            etUserLocation.setBackground(context.getResources().getDrawable(R.drawable.round_white));

        return true;

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

    //Select image and set to imageView

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE || requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            if (requestCode == IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    Bundle b = data.getExtras();
                    assert b != null;
                    bitmap = (Bitmap) b.getParcelable("data");
                    assert bitmap != null;
                    uriUserProfile = saveInGallery(bitmap);

                    //get Image Name
                    CGetImageName objCGetImageNAme = new CGetImageName(context);
                    sUserProfileName = objCGetImageNAme.getImageName(uriUserProfile);

                    try {
                        Glide.with(context).load(uriUserProfile).into(ivUserProfile);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else { // Result was a failure
                    Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (data.getData() != null) {
                    uriUserProfile = data.getData();
                    //get Image Name
                    CGetImageName objCGetImageNAme = new CGetImageName(context);
                    sUserProfileName = objCGetImageNAme.getImageName(uriUserProfile);

                    try {
                        Glide.with(context).load(uriUserProfile).into(ivUserProfile);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "Picture wasn't Selected!", Toast.LENGTH_SHORT).show();
                }

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
