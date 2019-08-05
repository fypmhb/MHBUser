package com.example.mhbuser.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.mhbuser.Adapters.DashBoardAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class CGetDataFromFireBaseForDashBoard {

    private FirebaseFirestore firebaseFirestore = null;
    public Context context;
    private ProgressDialog progressDialog = null;
    Boolean flage = false;


    public CGetDataFromFireBaseForDashBoard(Context context) {
        this.context = context;

    }

    public void prepareCardViewDashboard(final String fregmentName, final List<CDashBoardData> albumList, final DashBoardAdapter adapter) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseFirestore.collection(fregmentName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            assert document != null;
                            if (!document.isEmpty()) {

                                for (final DocumentSnapshot document1 : document) {

                                    firebaseFirestore.collection(fregmentName)
                                            .document(document1.getId())
                                            .collection(fregmentName + " info")
                                            .document(fregmentName + " Document")
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                DocumentSnapshot document2 = task.getResult();
                                                final CDashBoardData cDashBoardData = document2.toObject(CDashBoardData.class);
                                                cDashBoardData.setsUid(document1.getId());
                                                cDashBoardData.setsHallMarquee(fregmentName);


                                                //favourite

                                                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                firebaseFirestore.collection("Favourites")
                                                        .document(mAuth.getCurrentUser().getUid())
                                                        .collection(fregmentName+" id's")
                                                        .document(document1.getId())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot documentSnapshot = task.getResult();

                                                                    if (documentSnapshot.exists()) {
                                                                        cDashBoardData.setsFavourite("yess");
                                                                        albumList.add(cDashBoardData);
                                                                        adapter.notifyDataSetChanged();
                                                                    } else {
                                                                        cDashBoardData.setsFavourite("no");
                                                                        albumList.add(cDashBoardData);
                                                                        adapter.notifyDataSetChanged();

                                                                    }
                                                                }

                                                            }
                                                        });





                                            }
                                        }
                                    });
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void prepareCardViewFavourite(final String fregmentName, final List<CDashBoardData> albumList, final DashBoardAdapter adapter) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseFirestore.collection("Favourites")
                .document(mAuth.getCurrentUser().getUid())
                .collection(fregmentName+" id's")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            assert document != null;
                            if (!document.isEmpty()) {

                                for (final DocumentSnapshot document1 : document) {

                                    firebaseFirestore.collection(fregmentName)
                                            .document(document1.getId())
                                            .collection(fregmentName + " info")
                                            .document(fregmentName + " Document")
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                DocumentSnapshot document2 = task.getResult();
                                                CDashBoardData cDashBoardData = document2.toObject(CDashBoardData.class);
                                                cDashBoardData.setsUid(document1.getId());
                                                cDashBoardData.setsHallMarquee(fregmentName);
                                                cDashBoardData.setsFavourite("yess");
                                                albumList.add(cDashBoardData);
                                                adapter.notifyDataSetChanged();

                                            }
                                        }
                                    });
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}