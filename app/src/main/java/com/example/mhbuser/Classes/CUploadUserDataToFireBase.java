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
    private String sUserProfileName = null;
    private String userId = null;
    String sPassword=null;
    private Uri oldPicUri=null;
    private CSignUpData cSignUpData=null;
    private String picDelete="no";



    public CUploadUserDataToFireBase(Context context, Uri uriUserProfile, String sUserProfileName, String password,
                                     CSignUpData cSignUpData
                                     ) {

        this.context = context;
        this.uriUserProfile = uriUserProfile;
        this.sUserProfileName = sUserProfileName;
        this.sPassword=password;
        this.cSignUpData=cSignUpData;
        this.progressDialog = new ProgressDialog(context);
        this.storageReference = getInstance().getReference();

            createUserOnFireBase();

    }
    public CUploadUserDataToFireBase(Context context, Uri uriUserProfile, String sUserProfileName,Uri oldPicUri,
                                     CSignUpData cSignUpData
    ) {

        this.context = context;
        this.uriUserProfile = uriUserProfile;
        this.sUserProfileName = sUserProfileName;
        this.cSignUpData=cSignUpData;
        this.oldPicUri=oldPicUri;
        this.progressDialog = new ProgressDialog(context);
        this.storageReference = getInstance().getReference();


            this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            picDelete="yess";
            uploadDataToFireBase();
    }
    public CUploadUserDataToFireBase(Context context, CSignUpData cSignUpData) {

        this.context = context;
        this.cSignUpData=cSignUpData;
        this.progressDialog = new ProgressDialog(context);
        this.storageReference = getInstance().getReference();

        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        uploadData();
    }




    public void createUserOnFireBase() {

        progressDialog.setMessage("Creating User...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(cSignUpData.getsEmail(),sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userId = task.getResult().getUser().getUid();

                            progressDialog.dismiss();

                            uploadDataToFireBase();

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

    private void uploadDataToFireBase() {
        //create storageReference
        storageReference = this.storageReference.child("Users")
                .child(userId);
        uploadUserProfileImage();
    }


    private void uploadUserProfileImage() {

        progressDialog.setMessage("Uploading User Profile...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        if(picDelete.equals("yess"))
        {
            StorageReference storageReference = getInstance().getReferenceFromUrl(oldPicUri.toString());
            storageReference.delete();

        }

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

                        cSignUpData.setsUserProfileImageUri(downloadUri.toString());
                        progressDialog.dismiss();

                        uploadData();

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

    private void uploadData() {

        //setting progress bar title
        progressDialog.setMessage("Registering...");
        //show progress dialog
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users")
                .document(userId)
                .set(cSignUpData)
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