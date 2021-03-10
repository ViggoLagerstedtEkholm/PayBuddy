package com.example.paybuddy.Settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paybuddy.R;

/**
 *  This fragment displays a Info screen.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * This method inflates our "fragment_info.xml" view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the inflated view is returned.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    /**
     * This method gets the button from the view and adds a OnClick listener to the button.
     * If we click the button we navigate from the Info fragment to the tabViewFragment.
     * @param view the fragment inflated view.
     * @param savedInstanceState recently saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonBackInfo = view.findViewById(R.id.buttonBackInfo);
        buttonBackInfo.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_infoFragment_to_tabViewFragment));
    }
}