package com.example.paybuddy.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.R;
import com.google.android.material.tabs.TabLayout;

/**
 * This fragment class displays a ViewPager that shows "Settings, Home, Your Occasions" in the app.
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class TabViewFragment extends Fragment {

    public TabViewFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * This method inflates our "fragment_tab_view.xml" view.
     * @param inflater inflates our view.
     * @param container view that contains other views.
     * @param savedInstanceState saved instance for this fragment.
     * @return the inflated view is returned.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_view, container, false);
    }

    /**
     * When the view is created we create the viewpager and create the tabs.
     * @param view the view that has been inflated for this fragment.
     * @param savedInstanceState saved instance for this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Inflate the layout for this fragment
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Get the viewpager from the view.
        ViewPager viewPager = requireActivity().findViewById(R.id.view_pager);
        //Set the adapter for this viewpager.
        viewPager.setAdapter(sectionsPagerAdapter);
        //Get the tabs from our view.
        TabLayout tabs = requireActivity().findViewById(R.id.tabs);
        //Add a viewpager to our tabs.
        tabs.setupWithViewPager(viewPager);
        //Set the item index to 1.
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);
    }
}