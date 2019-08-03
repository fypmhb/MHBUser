package com.example.mhbuser.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mhbuser.Classes.CBookingData;
import com.example.mhbuser.Classes.CDashBoardData;
import com.example.mhbuser.Classes.CSubHallData;
import com.example.mhbuser.Classes.CUplaodBookingDetailToFireBase;
import com.example.mhbuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

public class BookingDetail extends AppCompatActivity implements View.OnClickListener {

    private CDashBoardData dashBoardObject=null;
    private CSubHallData subHallObject=null;

    private TextView
            tvHallName=null,
            tvSubHallName=null,
            tvSelectDate =null,
            tvPerHead=null,
            tvEstimatedBudget=null;
    private EditText
            etNumberOfGuest=null,
            etOtherDetail=null;
    private RadioGroup
            rgTiming=null,
            rgDish=null;
    private Button btnBooking=null;
    private SharedPreferences sharedPreferences=null;

    private String sHallName =null;
    private String sSubHallName=null;
    private String sFunctionDate=null;
    private String sTiming=null;
    private String sNumberOfGuest="0";
    private String sDish="Chicken";
    private String sPerHead=null;
    private String sEstimatedBudget=null;
    private String sOtherDetail=null;
    private int noOfGuest,hallCapacity=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        connectivity();
        settingViewData();

        tvSelectDate.setOnClickListener(this);
        btnBooking.setOnClickListener(this);
        rgDish.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 int rbCheckedId = rgDish.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(rbCheckedId);
                 sDish=rb.getText().toString();
                //Toast.makeText(BookingDetail.this, ""+sDish, Toast.LENGTH_SHORT).show();
                 perHaed(sDish);
                 if(!etNumberOfGuest.getText().toString().equals(""))
                 {
                     estimatedBudget(sNumberOfGuest,sPerHead);
                 }
            }
        });
        etNumberOfGuest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0)
                {
                    tvEstimatedBudget.setText("00");
                    sNumberOfGuest=null;
                }
                else
                {
                    sNumberOfGuest=etNumberOfGuest.getText().toString();
                    estimatedBudget(sNumberOfGuest,sPerHead);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void connectivity() {
        tvHallName=findViewById(R.id.tv_hall_name);
        tvSubHallName=findViewById(R.id.tv_sub_hall_name);
        tvSelectDate=findViewById(R.id.tv_select_date);
        tvPerHead=findViewById(R.id.tv_per_head);
        tvEstimatedBudget=findViewById(R.id.tv_estimated_budget);
        etNumberOfGuest=findViewById(R.id.et_no_of_guest);
        etOtherDetail=findViewById(R.id.et_other_detail);
        rgTiming=findViewById(R.id.rg_timing);
        rgDish=findViewById(R.id.rg_dish);
        btnBooking=findViewById(R.id.btn_hall_booking);

        //getting data from sharedPreference
        sharedPreferences = getSharedPreferences("MHBUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("HallGeneralDetail", "");
        dashBoardObject= gson.fromJson(json,CDashBoardData.class);
        String json1 = sharedPreferences.getString("SubHallDetail", "");
        subHallObject= gson.fromJson(json1,CSubHallData.class);

        sHallName=dashBoardObject.getsHallMarqueeName();
        sSubHallName=subHallObject.getsSubHallName();


    }

    private void settingViewData() {

        tvHallName.setText(sHallName);
        tvSubHallName.setText(sSubHallName);
        //for perHead and estimatedBudget
        perHaed(sDish);


    }

    private void perHaed(String dish) {

        switch (dish) {
            case "Chicken":
                sPerHead = subHallObject.getsChickenPerHead();
                tvPerHead.setText(sPerHead);
                break;
            case "Beef":
                sPerHead = subHallObject.getsBeefPerHead();
                tvPerHead.setText(sPerHead);
                break;
            case "Mutton":
                sPerHead = subHallObject.getsMuttonPerHead();
                tvPerHead.setText(sPerHead);
                break;
        }

    }

    private void estimatedBudget(String sNumberOfGuest, String sPerHead) {

        int guest=Integer.parseInt(sNumberOfGuest);
        int perHead=Integer.parseInt(sPerHead);
        sEstimatedBudget=""+((guest*perHead)+2000);
        tvEstimatedBudget.setText(sEstimatedBudget);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tv_select_date) {
            datePicker();
        }
        if(view.getId()==R.id.btn_hall_booking)
        {
            hallBooking();
        }

    }

    public void datePicker()
    {
        Calendar calendar=Calendar.getInstance();
        Calendar maxCalander = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(
                BookingDetail.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Month is 0 based, just add 1
                sFunctionDate=day+"/"+(month+1)+"/"+year;
                tvSelectDate.setText(sFunctionDate);
            }
        },year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(5,151,14)));

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        } else {
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        maxCalander.add(Calendar.MONTH,6);
        dialog.getDatePicker().setMaxDate(maxCalander.getTimeInMillis());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void hallBooking() {
        noOfGuest=Integer.parseInt(sNumberOfGuest);
        hallCapacity=Integer.parseInt(subHallObject.getsHallCapacity());
        int time=rgTiming.getCheckedRadioButtonId();
        RadioButton rbTime=(RadioButton) findViewById(time);
        sTiming=rbTime.getText().toString();
        sOtherDetail=etOtherDetail.getText().toString();

        if(sFunctionDate == null)
        {
            tvSelectDate.setBackground(getResources().getDrawable(R.drawable.round_red));
            return;
        }else
            tvSelectDate.setBackground(getResources().getDrawable(R.drawable.round_white));
        if(sNumberOfGuest.equals("0") ||noOfGuest>hallCapacity)
        {
            etNumberOfGuest.setBackground(getResources().getDrawable(R.drawable.round_red));
            Toast.makeText(this, "Please choose number of guest.Hall Capacity is: "+hallCapacity, Toast.LENGTH_SHORT).show();
            return;
        }else
            etNumberOfGuest.setBackground(getResources().getDrawable(R.drawable.round_white));


        uploadBookingDetail();
//        Toast.makeText(this, "Your request is sent to the "+sHallName+" manager for "+sNumberOfGuest+" number of guest on "+sFunctionDate, Toast.LENGTH_LONG).show();
//
    }

    private void uploadBookingDetail() {


        Date currentTime = Calendar.getInstance().getTime();
        String sBookingTimeDate = currentTime.toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        CBookingData cBookingData=new CBookingData(sBookingTimeDate,sSubHallName,sFunctionDate,sNumberOfGuest,sTiming,sDish,sPerHead,sEstimatedBudget,sOtherDetail);

        new CUplaodBookingDetailToFireBase(this,dashBoardObject.getsHallMarquee(),dashBoardObject.getsUid()
                ,subHallObject.getsSubHallUid()
                ,mAuth.getCurrentUser().getUid()
                ,cBookingData);

    }

}
