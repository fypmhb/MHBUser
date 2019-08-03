package com.example.mhbuser.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.mhbuser.Adapters.ImageSwipeAdapter;
import com.example.mhbuser.Classes.CDashBoardData;
import com.example.mhbuser.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;



public class HallMarqueeGenaralDetail extends AppCompatActivity {

    private TextView
            tvHallName = null,
            tvLocation = null,
            tvManagerName = null,
            tvPhoneNo = null,
            tvEmail = null,
            tvParking = null,
            tvAcHeater = null,
            tvMusic = null,
            tvSpotlight = null;
    private FloatingActionButton floatingActionButton;

    private SharedPreferences sp;

    private ViewPager hallEntranceImageViewPager = null;

    private CDashBoardData cDashBoardData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_marquee_genaral_detail);

        connectivity();
        setingDetail();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HallMarqueeGenaralDetail.this, HallMarqueeShowDetail.class);
                startActivity(i);
            }
        });
    }

    private void connectivity() {

        tvHallName = (TextView) findViewById(R.id.tv_hall_name);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvManagerName = (TextView) findViewById(R.id.tv_manager_name);
        tvPhoneNo = (TextView) findViewById(R.id.tv_phone_no);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvParking = (TextView)findViewById(R.id.tv_parking);
        tvAcHeater = (TextView) findViewById(R.id.tv_ac_heater);
        tvMusic = (TextView) findViewById(R.id.tv_music);
        tvSpotlight = (TextView) findViewById(R.id.tv_spotlights);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.btn_sub_hall_arrow) ;


        hallEntranceImageViewPager = (ViewPager) findViewById(R.id.hall_images_view_pager);


        sp = getSharedPreferences("MHBUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("HallGeneralDetail", "");
        cDashBoardData= gson.fromJson(json,CDashBoardData.class);

    }

    private void setingDetail() {

        ImageSwipeAdapter imageSwipeAdapter = new ImageSwipeAdapter(this, cDashBoardData.getsLHallEntranceDownloadImagesUri());
        hallEntranceImageViewPager.setAdapter(imageSwipeAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 3000, 4000);

        tvHallName.setText(cDashBoardData.getsHallMarqueeName());
        tvLocation.setText(cDashBoardData.getsLocation());
        tvManagerName.setText(cDashBoardData.getsManagerFirstName()+" "+cDashBoardData.getsManagerLastName());
        tvPhoneNo.setText(cDashBoardData.getsPhoneNo());
        tvEmail.setText(cDashBoardData.getsEmail());
        tvParking.setText(cDashBoardData.getsParking());
        tvAcHeater.setText(cDashBoardData.getsAc_Heater());
        tvMusic.setText(cDashBoardData.getsMusic());
        tvSpotlight.setText(cDashBoardData.getsSpotLights());

    }
    public class MyTimerTask extends TimerTask {

        int numberOfHallEntranceImages = cDashBoardData.getsLHallEntranceDownloadImagesUri().size() - 1;
        int i = 0;

        @Override
        public void run() {
            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hallEntranceImageViewPager.getCurrentItem() == i) {
                            i = i + 1;
                            hallEntranceImageViewPager.setCurrentItem(i);
                        } else if (i == numberOfHallEntranceImages + 1) {
                            i = 0;
                            hallEntranceImageViewPager.setCurrentItem(i);
                        }
                    }
                });
            }
        }
    }
}

