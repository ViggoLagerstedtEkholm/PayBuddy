package com.example.paybuddy.Selector.Tab;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.paybuddy.History.List.ListFragmentHistory;
import com.example.paybuddy.Home.HomeFragment;
import com.example.paybuddy.Occasions.List_of_occasions.ListFragmentOccasions;
import com.example.paybuddy.R;
import com.example.paybuddy.Selector.SelectorFragment;
import com.example.paybuddy.Settings.ManageFragment;
import com.example.paybuddy.TimesUp.TimesUp.List.ListFragmentDuePayment;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SelectorPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_occasions, R.string.tab_history, R.string.tab_expired};
    private final Context mContext;

    public SelectorPagerAdapter(Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment routedFragment = null;
        switch(position){
            case 0: routedFragment = new ListFragmentOccasions();
                break;
            case 1:
                routedFragment = new ListFragmentHistory();
                break;
            case 2:
                routedFragment = new ListFragmentDuePayment();
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