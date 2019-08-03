package com.example.mhbuser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mhbuser.Activities.HallMarqueeGenaralDetail;
import com.example.mhbuser.Classes.CDashBoardData;
import com.example.mhbuser.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;


public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.MyViewHolder> {


    private Context mContext;
    private List<CDashBoardData> albumList;
    SharedPreferences sp;



    public DashBoardAdapter(Context mContext, List<CDashBoardData> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HallName,Loaction,PhoneNo,Email;
        public ToggleButton favourite;
        public ImageView imageView;

        public MyViewHolder(@NonNull View view) {
            super(view);

            HallName = (TextView) view.findViewById(R.id.tv_cardView_hallName);
            Loaction = (TextView) view.findViewById(R.id.tv_cardView_location);
            PhoneNo = (TextView) view.findViewById(R.id.tv_cardView_phone_no);
            Email = (TextView) view.findViewById(R.id.tv_cardView_email);


            favourite=(ToggleButton)view.findViewById(R.id.iv_cardView_favourite);
            imageView = (ImageView) view.findViewById(R.id.iv_cardView);
            sp = mContext.getSharedPreferences("MHBUser", Context.MODE_PRIVATE);

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CDashBoardData album = albumList.get(position);



        final String hallName = album.getsHallMarqueeName();
        holder.HallName.setText(hallName);
        final String location = album.getsLocation();
        holder.Loaction.setText(location);
        final String phoneNo = album.getsPhoneNo();
        holder.PhoneNo.setText(phoneNo);
        final String email = album.getsEmail();
        holder.Email.setText(email);
       String hallpic=album.getsLHallEntranceDownloadImagesUri().get(0);

        final String Favourite  = album.getsFavourite();
        //Toast.makeText(mContext, ""+Favourite, Toast.LENGTH_SHORT).show();
        if(Favourite!=null&&Favourite.equals("yess"))
        {
            holder.favourite.setBackgroundResource(R.drawable.favourite_toggle);
            holder.favourite.setChecked(true);
        }
        else
        {
            holder.favourite.setChecked(false);
        }
        holder.Email.setText(email);



        Glide.with(mContext)
                .load(hallpic)
                .into(holder.imageView);



        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.favourite.isChecked()) {

                    holder.favourite.setBackgroundResource(R.drawable.favourite_toggle);
                    holder.favourite.setChecked(true);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", album.getsUid());

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore fBFS = FirebaseFirestore.getInstance();
                    fBFS.collection("Favourites")
                            .document(("fav"+(mAuth.getCurrentUser()).getUid())).collection(album.getsHallMarquee()).document(album.getsUid()).set(hashMap);



                } else {

                    //Toast.makeText(mContext, ""+holder.favourite.isChecked(), Toast.LENGTH_SHORT).show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("Favourites")
                            .document(mAuth.getCurrentUser().getUid()).collection(album.getsHallMarquee()).document(album.getsUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

                }
            }
        });

        //for next activity
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sp.edit();

                Gson gson = new Gson();
                String dashBoardObject = gson.toJson(album);
                editor.putString("HallGeneralDetail", dashBoardObject);
                editor.commit();

                Intent i=new Intent(mContext, HallMarqueeGenaralDetail.class);
                mContext.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}