package com.example.mhbuser.Activities;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.mhbuser.Fragments.FDashBoard;
import com.example.mhbuser.R;
import com.google.android.material.tabs.TabLayout;

public class FavouritesHallMarquee extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static Activity fin=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_hall_marquee);
        fin=this;

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.tabs_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {



            switch (position) {
                case 0: {

                    Bundle bundle = new Bundle();
                    bundle.putString("sHallMarquee","Hall");
                    bundle.putString("sDashboardFavourite","Favourite");
                    FDashBoard fragment = new FDashBoard();
                    fragment.setArguments(bundle);
                    return fragment;
                }
                case 1: {

                    Bundle bundle = new Bundle();
                    bundle.putString("sHallMarquee","Marquee");
                    bundle.putString("sDashboardFavourite","Favourite");
                    FDashBoard fragment = new FDashBoard();
                    fragment.setArguments(bundle);
                    return fragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

}