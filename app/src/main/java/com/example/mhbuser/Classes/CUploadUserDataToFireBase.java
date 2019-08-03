package com.example.mhbuser.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.mhbuser.Activities.DashBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class CUploadUserDataToFireBase {

    private ProgressDialog progressDialog = null;
    private Context context = null;
    private Uri uriUserProfile = null;
    private StorageReference storageReference;

    private String sUserEmail = null,
            sPassword = null,
            sUserProfileName = null,
            sUserProfileDownloadUri,
            sUserFirstName = null,
            sUserLastName = null,
            sUserPhoneNo = null,
            sUserCity = null,
            sUserLocation = null;

    private String userId = null;


    public CUploadUserDataToFireBase(Context context, Uri uriUserProfile, String sUserEmail,
                                     String sPassword, String sUserProfileName,
                                     String sUserProfileDownloadUri,
                                     String sUserFirstName, String sUserLastName,
                                     String sUserPhoneNo, String sUserCity,
                                     String sUserLocation) {

        this.context = context;
        this.uriUserProfile = uriUserProfile;
        this.sUserEmail = sUserEmail;
        this.sPassword = sPassword;
        this.sUserProfileName = sUserProfileName;
        this.sUserProfileDownloadUri = sUserProfileDownloadUri;
        this.sUserFirstName = sUserFirstName;
        this.sUserLastName = sUserLastName;
        this.sUserPhoneNo = sUserPhoneNo;
        this.sUserCity = sUserCity;
        this.sUserLocation = sUserLocation;

        this.progressDialog = new ProgressDialog(context);
        this.storageReference = getInstance().getReference();

        if (!this.sPassword.isEmpty())
            createUserOnFireBase();
        else {
            this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            uploadDataToFireBase(userId);
        }
    }

    public void createUserOnFireBase() {

        progressDialog.setMessage("Creating User...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(sUserEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userId = task.getResult().getUser().getUid();

                            progressDialog.dismiss();

                            uploadDataToFireBase(userId);

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(context, "Your are already registered", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void uploadDataToFireBase(String userId) {
        //create storageReference
        storageReference = this.storageReference.child("Users")
                .child(userId);
        uploadUserProfileImage(userId);
    }


    private void uploadUserProfileImage(final String userId) {

        progressDialog.setMessage("Uploading User Profile...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        StorageReference newStorageReference = storageReference.child("Profile");

        //adding addOnSuccessListener to storageReference2nd
        newStorageReference.child(sUserProfileName)
                .putFile(uriUserProfile)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        final Uri downloadUri = uriTask.getResult();

                        sUserProfileDownloadUri = downloadUri.toString();
                        progressDialog.dismiss();

                        uploadData(userId);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error. get and show error message
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void uploadData(final String userId) {

        //setting progress bar title
        progressDialog.setMessage("Registering...");
        //show progress dialog
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        CSignUpData CSignUpData = new CSignUpData(sUserFirstName, sUserLastName,
                sUserProfileDownloadUri, sUserEmail, sUserPhoneNo, sUserCity, sUserLocation);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users")
                .document(userId)
                .set(CSignUpData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();
                        context.startActivity(new Intent(context, DashBoard.class));
                        ((Activity) context).finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //error. get and show error message
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }


}