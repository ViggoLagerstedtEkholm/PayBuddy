package com.example.paybuddy.Settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.paybuddy.MVVM.ItemsViewModel;
import com.example.paybuddy.MVVM.OccasionViewModel;
import com.example.paybuddy.Occasions.Dialogs.DialogAddItem;
import com.example.paybuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {
    private boolean isSelected;
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
        buttonDeleteAllOccasions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingDialog.startLoading();

                DialogConfirm dialogFragment = new DialogConfirm();
                dialogFragment.show(getChildFragmentManager(), "Test");
            }
        });

        Switch switchDarkModeHome = (Switch) view.findViewById(R.id.switchDarkModeHome);
        switchDarkModeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected){
                    isSelected = false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    isSelected = true;
                }
            }
        });
    }
}