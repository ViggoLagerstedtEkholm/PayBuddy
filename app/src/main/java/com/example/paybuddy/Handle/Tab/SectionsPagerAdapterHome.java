package com.example.paybuddy.Handle.Tab;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.paybuddy.Handle.OccasionsFragment;
import com.example.paybuddy.History.HistoryFragment;
import com.example.paybuddy.Occasions.List_of_occasions.ListFragmentOccasions;
import com.example.paybuddy.R;
import com.example.paybuddy.TimesUp.DuePaymentFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterHome extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Tab1, R.string.Tab2, R.string.Tab3};
    private final Context mContext;

    public SectionsPagerAdapterHome(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment routedFragment = null;
        switch(position){
            case 0:
                routedFragment = new ListFragmentOccasions();
                break;
            case 1:
                routedFragment = new HistoryFragment();
                break;
            case 2:
                routedFragment = new DuePaymentFragment();
                break;
        }
        return routedFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}