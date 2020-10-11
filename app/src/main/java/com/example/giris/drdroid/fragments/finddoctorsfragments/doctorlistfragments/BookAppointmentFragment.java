package com.example.giris.drdroid.fragments.finddoctorsfragments.doctorlistfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giris.drdroid.R;
import com.example.giris.drdroid.fragments.finddoctorsfragments.DoctorListFragment;
import com.example.giris.drdroid.fragments.finddoctorsfragments.DoctorMapFragment;
import com.example.giris.drdroid.fragments.finddoctorsfragments.doctorlistfragments.bookappointmentfragments.BookFragment;
import com.example.giris.drdroid.fragments.finddoctorsfragments.doctorlistfragments.bookappointmentfragments.ReviewsFragment;

/**
 * Created by Ratan on 7/27/2015.
 */
public class BookAppointmentFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        if (container != null) {
            container.removeAllViews();
        }

        View x =  inflater.inflate(R.layout.fragment_find_doctors,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new BookFragment();
                case 1 : return new ReviewsFragment();
                //case 2 : return new RefundStatusFragment();
                //case 3 : return new ChequeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Book";
                case 1 :
                    return "Reviews";
                //case 2 :
                //   return "Refund Status";
                //case 3:
                //   return "HUL Cheques";
            }
            return null;
        }
    }

}
