package com.example.paybuddy.Selector.Tab;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.paybuddy.History.List.ListFragmentHistory;
import com.example.paybuddy.Occasions.List_of_occasions.ListFragmentOccasions;
import com.example.paybuddy.R;
import com.example.paybuddy.TimesUp.TimesUp.List.ListFragmentDuePayment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class SelectorPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_occasions, R.string.tab_history, R.string.tab_expired};
    private final Context mContext;

    /**
     * Constructor for this class.
     * @param context the application context
     * @param fm Fragment Manager.
     * @param behavior behavior.
     */
    public SelectorPagerAdapter(Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
    }

    /**
     *  This method returns a Fragment on the selected ViewPager index.
     *
     * @param position the current index in the ViewPager.
     * @return Fragment at the item index.
     */
    @NotNull
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
        return Objects.requireNonNull(routedFragment);
    }

    /**
     * Gets the viewpager titles "Settings", "Home", "Occasions".
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