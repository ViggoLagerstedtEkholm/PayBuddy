package com.example.paybuddy.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.paybuddy.Home.HomeFragment;
import com.example.paybuddy.Selector.SelectorFragment;
import com.example.paybuddy.Settings.ManageFragment;
import com.example.paybuddy.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //This String array holds our tab titles.
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.handle_app, R.string.tab_home, R.string.tab_selector};
    private final Context mContext;

    /**
     * Constructor for this class.
     * @param context the application context
     * @param fm Fragment Manager.
     * @param behavior
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    /**
     *  This method returns a Fragment on the selected ViewPager index.
     * @param position the current index in the ViewPager.
     * @return Fragment at the item index.
     */
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

    /**
     * Gets the viewpager titles "Settings", "Home", "Your Occasions".
     * @param position in the ViewPager.
     * @return CharSequence that holds the characters for our index.
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * This method returns the total amount of items in our ViewPager.
     * @return int number of tabs in our TAB_TITLES String array.
     */
    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}