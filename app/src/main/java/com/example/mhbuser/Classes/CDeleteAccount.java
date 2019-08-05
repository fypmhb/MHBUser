package com.example.mhbuser.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.mhbuser.Activities.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.StorageReference;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class CDeleteAccount {


    private Context context = null;

    //FireBase Work
    private ProgressDialog progressDialog = null;


    private CSignUpData cSignUpData = null;

    private String userId = null;

    private FirebaseFirestore firebaseFirestore = null;

    public CDeleteAccount(Context context) {
        this.context = context;

        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //getDataFromFireBase();
        deleteRequestData();

    }


    private void getDataFromFireBase() {
        final DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(userId);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            cSignUpData = documentSnapshot.toObject(CSignUpData.class);
                            deleteProfileImage();
                        }
                    }
                });
    }


    private void deleteProfileImage() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting User Profile Image...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        StorageReference storageReference = getInstance().getReferenceFromUrl(cSignUpData.getsUserProfileImageUri());
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteHallFromFireStore();
                        progressDialog.dismiss();
                    }
                });
    }

    private void deleteHallFromFireStore() {
        progressDialog.setMessage("Deleting Hall...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(userId);


        documentReference.delete().
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteRequestData();

                    }
                });
    }


    private void deleteRequestData() {


        final CollectionReference collectionReference = firebaseFirestore
                .collection("Booking Requests")
                .document(userId)
                .collection("Hall");

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            QuerySnapshot document = task.getResult();
                            assert document != null;
                            if (!document.isEmpty())
                            {
                                Toast.makeText(context, ""+document, Toast.LENGTH_SHORT).show();

                                for ( final DocumentSnapshot documentSnapshot : document) {
                                    Toast.makeText(context, ""+documentSnapshot.getId(), Toast.LENGTH_SHORT).show();

                                    final CollectionReference collectionReference1 = collectionReference
                                            .document(documentSnapshot.getId())
                                            .collection("Sub Halls");
                                    collectionReference1
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot document2 = task.getResult();
                                                        assert document2 != null;
                                                        if (!document2.isEmpty()) {
                                                            for (final DocumentSnapshot documentSnapshot1 : document2) {
                                                                Toast.makeText(context, ""+documentSnapshot1.getId(), Toast.LENGTH_SHORT).show();

                                                                collectionReference1
                                                                        .document(documentSnapshot1.getId())
                                                                        .delete()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                //deleteCurrentUser();
                                                                                Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }

                                                }
                                            });


                                }
                            }
                        }

                    }
                });
    }

    private void deleteCurrentUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.delete().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            context.startActivity(new Intent(context, SplashScreen.class));
                            ((Activity) context).finish();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}

