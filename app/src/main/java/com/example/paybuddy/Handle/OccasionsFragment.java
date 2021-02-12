package com.example.paybuddy.Handle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paybuddy.Handle.Tab.SectionsPagerAdapterHome;
import com.example.paybuddy.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OccasionsFragment extends Fragment {
    private Button buttonAdd;

    public OccasionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SectionsPagerAdapterHome sectionsPagerAdapter = new SectionsPagerAdapterHome(getContext(), getParentFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pagerHome);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabsHome);
        tabs.setupWithViewPager(viewPager);

        buttonAdd = (Button) view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_occasionAddFragment);
            }
        });
    }
}