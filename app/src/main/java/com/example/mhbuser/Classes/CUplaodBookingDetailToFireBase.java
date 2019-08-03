package com.example.mhbuser.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.mhbuser.Activities.DashBoard;
import com.example.mhbuser.Activities.HallMarqueeShowDetail;
import com.example.mhbuser.Notification.APIService;
import com.example.mhbuser.Notification.Sending.*;
import com.example.mhbuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CUplaodBookingDetailToFireBase {

    private  Context context=null;
    private String sHallMarquee=null
            ,sHallId=null
            ,sSubHallId=null
            ,sUserId=null;
    private CBookingData cBookingData=null;
    private ProgressDialog progressDialog=null;
    private APIService apiService=null;

    public CUplaodBookingDetailToFireBase(Context context,String sHallMarquee,String sHallId,String sSubHallId,String sUserId,CBookingData cBookingData) {

        this.context=context;
        this.sHallMarquee=sHallMarquee;
        this.sHallId=sHallId;
        this.sSubHallId=sSubHallId;
        this.sUserId=sUserId;
        this.cBookingData=cBookingData;
        progressDialog=new ProgressDialog(context);

        apiService= Client.getClient("https://fcm.googleapis.com/")
                .create(APIService.class);

        updateToken(FirebaseInstanceId.getInstance().getToken());

        uploadData();

    }

    private void uploadData() {

        progressDialog.setMessage("Registering...");
        //show progress dialog
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Booking Requests")
                .document(sHallMarquee)
                .collection(sHallId)
                .document(sSubHallId)
                .collection(sUserId)
                .document("Booking Details")
                .set(cBookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                   // sendNotification();
                    context.startActivity(new Intent(context, DashBoard.class));
                    ((Activity)context).finish();
                    HallMarqueeShowDetail.fin.finish();
                    DashBoard.fin.finish();

                }

            }
        });



    }
    private void updateToken(String token)
    {
        Token token1=new Token(token);
        FirebaseFirestore.getInstance()
        .collection("Tokens")
                .document(sUserId)
                .set(token1);


    }

    private void sendNotification() {

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Tokens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            QuerySnapshot document=task.getResult();
                            assert document != null;
                            for (QueryDocumentSnapshot querySnapshot:document){

                                if(querySnapshot.exists())
                                {
                                    if(querySnapshot.getId().equals(sUserId));
                                    Token token=(Token)querySnapshot.getData();
                                    Data data=new Data(sUserId, R.drawable.ic_admin_profile,"User Name","Hall Booking",sHallId);
                                    Sender sender=new Sender(data,token.getToken());
                                    apiService.sendNotification(sender)
                                            .enqueue(new Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                    if(response.code()==200)
                                                    {
                                                        if(response.body().success!=1)
                                                        {
                                                            Toast.makeText(context, "Notification not sent", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                            Toast.makeText(context, "Notification Sent", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                                }
                                            });
                                }
                            }
                        }
                    }
                });

    }
}
