package com.example.paybuddy.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.paybuddy.MVVM.ItemsViewModel;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.MVVM.OccasionViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private TextView textViewSumOfItems;
    private TextView textViewCountOfExpiredOccasions;
    private TextView textViewCountOfOccasions;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemsViewModel itemsViewModel;
    private OccasionViewModel occasionViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsViewModel = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
        occasionViewModel = new ViewModelProvider(requireActivity()).get(OccasionViewModel.class);
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

        occasionViewModel.getOccasionsWithItems().observe(getActivity(), items ->{
            int totalOccasions = items.size();
            textViewCountOfOccasions.setText(String.valueOf(totalOccasions));
            int totalExpired = calculateTotalExpired(items);
            textViewCountOfExpiredOccasions.setText(String.valueOf(totalExpired));
        });

        itemsViewModel.getItems().observe(getActivity(), items ->{
            double totalCost = calculateTotalCost(items);
            textViewSumOfItems.setText(Double.toString(totalCost));
        });

        swipeRefreshLayout = view.findViewById(R.id.refreshHomePage);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Updating fields", "...");
                //double totalPrice = databaseHelper.getSumItems();
                //int totalOccasion = databaseHelper.getAmountOfOccasion();
                //textViewSumOfItems.setText(Double.toString(totalPrice));
                //textViewCountOfOccasions.setText(String.valueOf(totalOccasion));
                //if(swipeRefreshLayout.isRefreshing()){
                 //   swipeRefreshLayout.setRefreshing(false);
                //}
            }
        });
    }

    private int calculateTotalExpired(List<OccasionWithItems> occasionModels){
        int total = 0;
        for(OccasionWithItems occasionWithItems : occasionModels){
            OccasionModel occasionModel = occasionWithItems.occasionModel;
            if(occasionModel.isExpired()){
                total++;
            }
        }


        return total;
    }

    private double calculateTotalCost(List<ItemModel> items){
        double total = 0;

        for(ItemModel itemModel : items){
            total += itemModel.getPrice() * itemModel.getQuantity();
        }

        return total;
    }
}