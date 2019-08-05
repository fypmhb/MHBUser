package com.example.mhbuser.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mhbuser.Adapters.DashBoardAdapter;
import com.example.mhbuser.Classes.CDashBoardData;
import com.example.mhbuser.Classes.CGetDataFromFireBaseForDashBoard;
import com.example.mhbuser.R;

import java.util.ArrayList;
import java.util.List;

public class FDashBoard extends Fragment {

    private DashBoardAdapter adapter;
    private List<CDashBoardData> albumList;
    private String sHallMarquee =null;
    private String sDashboardFavourite =null;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fregment_hall_marque_tabs, container, false);


        sHallMarquee = getArguments().getString("sHallMarquee");
        sDashboardFavourite = getArguments().getString("sDashboardFavourite");
        RecyclerView recyclerView = v.findViewById(R.id.recycle_view);
        albumList = new ArrayList<>();
        adapter = new DashBoardAdapter(getActivity(), albumList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        //getting data

        getData();

        return v;

    }

    private void getData() {
        CGetDataFromFireBaseForDashBoard cGetDataFromFireBaseForDashBoard = new CGetDataFromFireBaseForDashBoard(getActivity());

        if(sDashboardFavourite.equals("Dashboard"))
            cGetDataFromFireBaseForDashBoard.prepareCardViewDashboard(sHallMarquee, albumList, adapter);
        else
            cGetDataFromFireBaseForDashBoard.prepareCardViewFavourite(sHallMarquee, albumList, adapter);


    }



}