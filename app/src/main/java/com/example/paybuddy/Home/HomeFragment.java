package com.example.paybuddy.Home;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybuddy.Maps.CoordinatesViewModel;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private TextView textViewSumOfItems;
    private TextView textViewCountOfExpiredOccasions;
    private TextView textViewCountOfOccasions;
    private TextView textViewCountOfPaidOccasions;

    private ItemsViewModel itemsViewModel;
    private OccasionViewModel occasionViewModel;
    private CoordinatesViewModel coordinatesViewModel;
    private List<OccasionModel> occasionModels;

    public HomeFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsViewModel = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
        occasionViewModel = new ViewModelProvider(requireActivity()).get(OccasionViewModel.class);
        coordinatesViewModel = new ViewModelProvider(requireActivity()).get(CoordinatesViewModel.class);
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

        textViewCountOfExpiredOccasions = view.findViewById(R.id.textViewCountOfExpiredOccasions);
        textViewSumOfItems = view.findViewById(R.id.textViewSumOfItems);
        textViewCountOfOccasions = view.findViewById(R.id.textViewCountOfOccasions);
        textViewCountOfPaidOccasions = view.findViewById(R.id.textViewCountOfPaidOccasions);

        Button locationsButton = view.findViewById(R.id.buttonHomeSeeAllLocations);
        locationsButton.setOnClickListener(v -> {
            coordinatesViewModel.setLocations(occasionModels);
            Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_allOccasionsMapFragment);
        });

        occasionViewModel.getAllOccasions().observe(requireActivity(), occasionWithItems -> {
            List<OccasionModel> occasionModels = new ArrayList<>();

            for(OccasionWithItems occasionModel : occasionWithItems){
                OccasionModel aOccasionModel = occasionModel.occasionModel;
                aOccasionModel.setItems(occasionModel.itemModelList);
                aOccasionModel.setLocationModel(occasionModel.locationModel);

                occasionModels.add(aOccasionModel);
            }
            this.occasionModels = occasionModels;
        });

        occasionViewModel.getActiveOccasions().observe(requireActivity(), items ->{
            int totalOccasions = items.size();
            animate(totalOccasions, textViewCountOfOccasions);
        });

        occasionViewModel.getExpiredOccasions().observe(requireActivity(), items->{
            int totalExpired = items.size();
            animate(totalExpired, textViewCountOfExpiredOccasions);
        });

        occasionViewModel.getPaidOccasions().observe(requireActivity(), items ->{
            int totalPaid = items.size();
            animate(totalPaid, textViewCountOfPaidOccasions);
        });

        itemsViewModel.getTotalCost().observe(requireActivity(), items ->{
            if(items != null){
                animate(items, textViewSumOfItems);
            }
            else{
                textViewSumOfItems.setText("0.0");
            }
        });
    }

    private void animate(int end, TextView view)
    {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, end);
        animator.addUpdateListener(animation -> view.setText(String.valueOf(animation.getAnimatedValue())));
        animator.setDuration(600);
        animator.start();
    }
}