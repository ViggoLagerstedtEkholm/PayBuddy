package com.example.paybuddy.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;

    public ManageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(getActivity()).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //LoadingDialog loadingDialog = new LoadingDialog(getActivity());

        Button buttonDeleteAllOccasions = (Button) view.findViewById(R.id.buttonDeleteAllOccasions);
        Button buttonDeleteHistory = (Button) view.findViewById(R.id.buttonDeleteHistory);
        Button buttonDeleteExpired = (Button) view.findViewById(R.id.buttonDeleteAllExpired);
        Button buttonDeleteAll = (Button) view.findViewById(R.id.buttonWipeData);
        Button buttonInfo = (Button) view.findViewById(R.id.buttonInfo);

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_infoFragment);
            }
        });

        buttonDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeleteHistoryConfirm dialogFragment = new DialogDeleteHistoryConfirm();
                dialogFragment.show(getChildFragmentManager(), "Test");
            }
        });

        buttonDeleteAllOccasions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingDialog.startLoading();

                DialogDeleteOccasionsConfirm dialogFragment = new DialogDeleteOccasionsConfirm();
                dialogFragment.show(getChildFragmentManager(), "Test");
            }
        });

        buttonDeleteExpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeleteExpiredConfirm dialogDeleteExpiredConfirm = new DialogDeleteExpiredConfirm();
                dialogDeleteExpiredConfirm.show(getChildFragmentManager(), "Test");
            }
        });

        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeleteAllConfirm deleteAllConfirm = new DialogDeleteAllConfirm();
                deleteAllConfirm.show(getChildFragmentManager(), "Test");
            }
        });

        SwitchCompat switchDarkModeHome = (SwitchCompat) view.findViewById(R.id.switchDarkModeHome);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("night", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("night mode", true);
        Log.d("Im in here!", String.valueOf(booleanValue.booleanValue()));

        if(booleanValue.booleanValue()){
            Log.d("Im in here!", "abc");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchDarkModeHome.setChecked(true);
        }

        switchDarkModeHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchDarkModeHome.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night mode", true);
                    editor.commit();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchDarkModeHome.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night mode", false);
                    editor.commit();
                }
            }
        });
    }
}