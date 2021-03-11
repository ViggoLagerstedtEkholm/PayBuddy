package com.example.paybuddy.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paybuddy.R;
import com.example.paybuddy.Repositories.Repository;

/**
 *  This fragment handles all buttons and navigation on the "Settings" page.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class ManageFragment extends Fragment {

    public ManageFragment() {
        // Required empty public constructor
    }

    /**
     * This method inflates our "fragment_manage.xml" view.
     * @param inflater inflater for our view.
     * @param container contains other views.
     * @param savedInstanceState latest saved instance.
     * @return the inflated view is returned.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    /**
     * This method gets all the Buttons from the view and adds OnClickListeners to them.
     * Depending on what button was clicked we change the DELETE_TYPE enum passed into the DeleteDialog Dialog.
     * @param view the fragment inflated view.
     * @param savedInstanceState recently saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonDeleteAllOccasions = view.findViewById(R.id.buttonDeleteAllOccasions);
        Button buttonDeleteHistory = view.findViewById(R.id.buttonDeleteHistory);
        Button buttonDeleteExpired = view.findViewById(R.id.buttonDeleteAllExpired);
        Button buttonDeleteAll = view.findViewById(R.id.buttonWipeData);
        Button buttonInfo = view.findViewById(R.id.buttonInfo);

        buttonInfo.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_infoFragment));

        buttonDeleteHistory.setOnClickListener(v -> {
            DeleteDialog dialogFragment = new DeleteDialog(Repository.DELETE_TYPE.DELETE_ALL_HISTORY);
            dialogFragment.show(getChildFragmentManager(), "Test");
        });

        buttonDeleteAllOccasions.setOnClickListener(v -> {
            DeleteDialog dialogFragment = new DeleteDialog(Repository.DELETE_TYPE.DELETE_ALL_UNPAID);
            dialogFragment.show(getChildFragmentManager(), "Test");
        });

        buttonDeleteExpired.setOnClickListener(v -> {
            DeleteDialog dialogDeleteExpiredConfirm = new DeleteDialog(Repository.DELETE_TYPE.DELETE_ALL_EXPIRED);
            dialogDeleteExpiredConfirm.show(getChildFragmentManager(), "Test");
        });

        buttonDeleteAll.setOnClickListener(v -> {
            DeleteDialog deleteAllConfirm = new DeleteDialog(Repository.DELETE_TYPE.DELETE_ALL);
            deleteAllConfirm.show(getChildFragmentManager(), "Test");
        });

        SwitchCompat switchDarkModeHome = view.findViewById(R.id.switchDarkModeHome);
        handleDarkMode(switchDarkModeHome);
    }

    /**
     * This method checks if we have enabled dark mode with SharedPreferences.
     * If we have enabled dark mode this will recreate the activity on start.
     * We make the switch checked if we have enabled dark mode.
     * @param switchDarkModeHome the View switch element.
     */
    private void handleDarkMode(SwitchCompat switchDarkModeHome){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("night", 0);
        boolean booleanValue = sharedPreferences.getBoolean("night mode", true);

        if(booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchDarkModeHome.setChecked(true);
        }

        switchDarkModeHome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                switchDarkModeHome.setChecked(true);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night mode", true);
                editor.apply();
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                switchDarkModeHome.setChecked(false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night mode", false);
                editor.apply();
            }
        });
    }
}