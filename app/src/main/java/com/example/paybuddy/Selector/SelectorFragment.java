package com.example.paybuddy.Selector;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paybuddy.Selector.Tab.SelectorPagerAdapter;
import com.example.paybuddy.R;
import com.google.android.material.tabs.TabLayout;

/**
 * This fragment class displays a ViewPager that shows "Occasions, History, Expired" in the app.
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class SelectorFragment extends Fragment {

    public SelectorFragment() {
        // Required empty public constructor
    }

    /**
     * This method inflates our view.
     * @param inflater inflater for our view.
     * @param container view that contains other views.
     * @param savedInstanceState latest saved instance.
     * @return returns the inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selector, container, false);
    }

    /**
     * When the view is created we create the viewpager and create the tabs.
     * @param view the view that has been inflated for this fragment.
     * @param savedInstanceState saved instance for this fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create the SelectorPagerAdapter
        SelectorPagerAdapter sectionsPagerAdapter = new SelectorPagerAdapter(getActivity(), getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Get the ViewPager from the view.
        ViewPager viewPager = view.findViewById(R.id.view_pagerHome);
        //Set the adapter for this viewpager.
        viewPager.setAdapter(sectionsPagerAdapter);
        //Get the tabs from the view.
        TabLayout tabs = view.findViewById(R.id.tabsHome);
        //Add a viewpager to our tabs.
        tabs.setupWithViewPager(viewPager);
        //Set the item index to 0.
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

        //Add a button click listener that navigates us to the "Add Occasion" fragment on user click.
        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_occasionAddFragment));
    }
}