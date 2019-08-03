package com.example.mhbuser.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.mhbuser.Classes.CDashBoardData;
import com.example.mhbuser.Classes.CSubHallData;
import com.example.mhbuser.Fragments.FHallMarqueeShowDetail;
import com.example.mhbuser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.*;
import com.google.gson.Gson;

public class HallMarqueeShowDetail extends AppCompatActivity {
    private TabLayout tabLayout = null;
    private ViewPager tabsViewPager = null;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog = null;
    public static Activity fin;


    //FireBase work
    private String userId = null,
            sHallMarquee = null;

    private FirebaseFirestore firebaseFirestore = null;

    private int noOfTabs = 0;

    private CSubHallData CSubHallData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_marquee_show_detail);

        fin=this;

        connectivity();

        checkFireBaseState();
    }

    private void connectivity() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        sp = getSharedPreferences("MHBUser", Context.MODE_PRIVATE);
        editor=sp.edit();
        Gson gson = new Gson();
        String json = sp.getString("HallGeneralDetail", "");
        CDashBoardData dashBoardObject= gson.fromJson(json,CDashBoardData.class);

        sHallMarquee = dashBoardObject.getsHallMarquee();
        userId =dashBoardObject.getsUid();

        tabsViewPager = findViewById(R.id.detail_tabs_view_pager);

        tabLayout = findViewById(R.id.tl_hall_marquee_detail);

    }

    private void checkFireBaseState() {

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        assert sHallMarquee != null;
        firebaseFirestore.collection(sHallMarquee)
                .document(userId)
                .collection("Sub Hall info")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    int i = 1;
                    for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                        editor.putString("sSubHallDocumentId" + i, snapshots.getId());
                        editor.commit();
                        i += 1;
                    }
                    getSubHallCounter();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(HallMarqueeShowDetail.this, "Please add sub hall", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private void getSubHallCounter() {
        //Toast.makeText(this, ""+sHallMarquee+" "+userId, Toast.LENGTH_SHORT).show();

        DocumentReference documentReference = firebaseFirestore
                .collection(sHallMarquee)
                .document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Toast.makeText(HallMarqueeShowDetail.this, ""+document.exists(), Toast.LENGTH_SHORT).show();
                    assert document != null;
                    if (document.exists()) {
                        progressDialog.dismiss();
                        noOfTabs = Integer.parseInt(document.getString("Sub Hall Counter"));
                        //Toast.makeText(HallMarqueeShowDetail.this, ""+noOfTabs, Toast.LENGTH_SHORT).show();
                        setActivityHallMarqueeDetail(noOfTabs);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setActivityHallMarqueeDetail(int noOfTabs) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), noOfTabs);
        tabsViewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(tabsViewPager);
    }

    //Fragment Replacement Class
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private int noOfItems = 0;


        ViewPagerAdapter(FragmentManager fm, int noOfItems) {
            super(fm);
            this.noOfItems = noOfItems;
        }


        @Override
        public Fragment getItem(int position) {
            return FHallMarqueeShowDetail.newInstance(sp.getString("sSubHallDocumentId" + (position + 1), null));
        }
        @Override
        public int getCount() {
            return noOfItems;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Sub Hall " + (position + 1);
        }
    }

}