package com.example.paybuddy.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.R;
import com.example.paybuddy.Repositories.RepositoryViewModel;
import com.example.paybuddy.database.DatabaseHelper;
import com.example.paybuddy.database.FILTER_TYPE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private TextView textViewSumOfItems;
    private TextView textViewCountOfExpiredOccasions;
    private DatabaseHelper databaseHelper;
    private TextView textViewCountOfOccasions;
    private boolean isSelected;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RepositoryViewModel repositoryViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(getContext());
        repositoryViewModel = new ViewModelProvider(requireActivity()).get(RepositoryViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("View is being created!", "...");

        textViewCountOfExpiredOccasions = (TextView) view.findViewById(R.id.textViewCountOfExpiredOccasions);
        textViewSumOfItems = (TextView) view.findViewById(R.id.textViewSumOfItems);
        textViewCountOfOccasions = (TextView) view.findViewById(R.id.textViewCountOfOccasions);

        repositoryViewModel.init(getContext());
        double totalPrice = repositoryViewModel.getSumCost();
        int totalOccasion = repositoryViewModel.getSumOccasions();

        textViewSumOfItems.setText(Double.toString(totalPrice));
        textViewCountOfOccasions.setText(String.valueOf(totalOccasion));

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

        swipeRefreshLayout = view.findViewById(R.id.refreshHomePage);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Updating fields", "...");
                double totalPrice = databaseHelper.getSumItems();
                int totalOccasion = databaseHelper.getAmountOfOccasion();
                textViewSumOfItems.setText(Double.toString(totalPrice));
                textViewCountOfOccasions.setText(String.valueOf(totalOccasion));
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}