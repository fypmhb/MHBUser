package com.example.mhbuser.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.mhbuser.Activities.BookingDetail;
import com.example.mhbuser.Classes.CDashBoardData;
import com.example.mhbuser.Classes.CSubHallData;
import com.example.mhbuser.Adapters.ImageSwipeAdapter;
import com.example.mhbuser.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class FHallMarqueeShowDetail extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    static final String S_SUB_HALL_DOCUMENT_ID = "S_SUB_HALL_DOCUMENT_ID";

    private TextView tvAveragePerHeadRate = null,
            tvSubHallName = null,
            tvFloorNo = null,
            tvHallCapacity = null,
            tvSweetDish = null,
            tvSalad = null,
            tvDrink = null,
            tvNan = null,
            tvRise = null,
            tvChickenPerHead = null,
            tvMuttonPerHeadRate = null,
            tvBeefPerHeadRate = null;
    private Button btHallSelect=null;
    private SharedPreferences sp;

    private ProgressDialog progressDialog = null;

    private ViewPager imageViewPager = null;

    //FireBase work
    private String userId = null,
            sHallMarquee = null;

    private FirebaseFirestore firebaseFirestore = null;

    private int noOfTabs = 0;
    private String sSubHallDocumentId = null;

    private CSubHallData cSubHallData = null;
    private CDashBoardData dashBoardObject=null;


    public FHallMarqueeShowDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sSubHallDocumentId = getArguments().getString(S_SUB_HALL_DOCUMENT_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fregment_hall_marquee_show_detail, container, false);

        //connectivity

        tvAveragePerHeadRate = (TextView) view.findViewById(R.id.tv_sub_hall_average_per_head_rate);
        tvSubHallName = (TextView) view.findViewById(R.id.tv_sub_hall_name);
        tvFloorNo = (TextView) view.findViewById(R.id.tv_floor_no);
        tvHallCapacity = (TextView) view.findViewById(R.id.tv_hall_capacity);
        tvSweetDish = (TextView) view.findViewById(R.id.tv_sweet_dish);
        tvSalad = (TextView) view.findViewById(R.id.tv_salad);
        tvDrink = (TextView) view.findViewById(R.id.tv_drink);
        tvNan = (TextView) view.findViewById(R.id.tv_nan);
        tvRise = (TextView) view.findViewById(R.id.tv_rise);
        tvChickenPerHead = (TextView) view.findViewById(R.id.tv_chicken_per_head_rate);
        tvMuttonPerHeadRate = (TextView) view.findViewById(R.id.tv_mutton_per_head_rate);
        tvBeefPerHeadRate = (TextView) view.findViewById(R.id.tv_beef_per_head_rate);
        btHallSelect=(Button)view.findViewById(R.id.hall_select);

        imageViewPager = (ViewPager) view.findViewById(R.id.images_view_pager);

        firebaseFirestore = FirebaseFirestore.getInstance();

        sp = this.getActivity().getSharedPreferences("MHBUser", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("HallGeneralDetail", "");
        dashBoardObject= gson.fromJson(json,CDashBoardData.class);

        sHallMarquee = dashBoardObject.getsHallMarquee();
        userId =dashBoardObject.getsUid();


        getDataFromFireBase();
        btHallSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();

                Gson gson = new Gson();
                String Object = gson.toJson(cSubHallData);
                editor.putString("SubHallDetail", Object);
                editor.commit();
                Toast.makeText(getActivity(), ""+cSubHallData.getsSubHallUid(), Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getActivity(), BookingDetail.class);
                startActivity(i);
            }
        });

        return view;
    }


    private void getDataFromFireBase() {

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Setting Details...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        final DocumentReference documentReference1 = firebaseFirestore
                .collection(sHallMarquee)
                .document(userId)
                .collection("Sub Hall info")
                .document(sSubHallDocumentId);

        documentReference1.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            progressDialog.dismiss();
                            cSubHallData = documentSnapshot.toObject(CSubHallData.class);
                            cSubHallData.setsSubHallUid(sSubHallDocumentId);
                            setFHallMarqueeDetail();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setFHallMarqueeDetail() {

       ImageSwipeAdapter imageSwipeAdapter = new ImageSwipeAdapter(getActivity(), cSubHallData.getsLGetAddHallImagesDownloadUri());
        imageViewPager.setAdapter(imageSwipeAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 3000, 4000);

        int chickenPerHead = Integer.parseInt(cSubHallData.getsChickenPerHead()),
                muttonPerHead = Integer.parseInt(cSubHallData.getsMuttonPerHead()),
                beefPerHead = Integer.parseInt(cSubHallData.getsBeefPerHead());
        double averagePerHeadRate = (chickenPerHead + muttonPerHead + beefPerHead) / 3;

        tvAveragePerHeadRate.setText("Average Per Head = " + averagePerHeadRate);
        tvSubHallName.setText(cSubHallData.getsSubHallName());
        tvFloorNo.setText(cSubHallData.getsSubHallFloorNo());
        tvHallCapacity.setText(cSubHallData.getsHallCapacity());
        tvSweetDish.setText(cSubHallData.getsSweetDish());
        tvSalad.setText(cSubHallData.getsSalad());
        tvDrink.setText(cSubHallData.getsDrink());
        tvNan.setText(cSubHallData.getsNan());
        tvRise.setText(cSubHallData.getsRise());
        tvChickenPerHead.setText(cSubHallData.getsChickenPerHead());
        tvMuttonPerHeadRate.setText(cSubHallData.getsMuttonPerHead());
        tvBeefPerHeadRate.setText(cSubHallData.getsBeefPerHead());
    }

    public static FHallMarqueeShowDetail newInstance(String sSubHallDocumentId) {
        FHallMarqueeShowDetail fragment = new FHallMarqueeShowDetail();
        Bundle args = new Bundle();
        args.putString(S_SUB_HALL_DOCUMENT_ID, sSubHallDocumentId);
        fragment.setArguments(args);
        return fragment;
    }

    // timer class for image swipe
    public class MyTimerTask extends TimerTask {

        int numberOfHallEntranceImages = cSubHallData.getsLGetAddHallImagesDownloadUri().size() - 1;
        int i = 0;

        @Override
        public void run() {
            if (getActivity() != null)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageViewPager.getCurrentItem() == i) {
                            i = i + 1;
                            imageViewPager.setCurrentItem(i);
                        } else if (i == numberOfHallEntranceImages + 1) {
                            i = 0;
                            imageViewPager.setCurrentItem(i);
                        }
                    }
                });
            }
        }
    }
}
