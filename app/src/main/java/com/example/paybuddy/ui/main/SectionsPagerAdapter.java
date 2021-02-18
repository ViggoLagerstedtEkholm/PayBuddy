package com.example.paybuddy.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.paybuddy.History.HistoryFragment;
import com.example.paybuddy.Home.HomeFragment;
import com.example.paybuddy.Selector.SelectorFragment;
import com.example.paybuddy.Settings.ManageFragment;
import com.example.paybuddy.TimesUp.TimesUp.DuePaymentFragment;
import com.example.paybuddy.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.handle_app, R.string.tab_home, R.string.tab_selector};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment routedFragment = null;
        switch(position){
            case 0: routedFragment = new ManageFragment();
                break;
            case 1:
                routedFragment = new HomeFragment();
                break;
            case 2:
                routedFragment = new SelectorFragment();
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