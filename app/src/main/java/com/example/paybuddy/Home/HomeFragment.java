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
 * This fragment displays the home screen of the app.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
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

    public HomeFragment(){
        // Required empty public constructor
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsViewModel = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
        occasionViewModel = new ViewModelProvider(requireActivity()).get(OccasionViewModel.class);
        coordinatesViewModel = new ViewModelProvider(requireActivity()).get(CoordinatesViewModel.class);
    }

    /**
     * This method inflates our view.
     * @param inflater inflater for our view.
     * @param container view that contains other views.
     * @param savedInstanceState latest saved instance.
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * This method is called when the fragment is created.
     * We observe data about total item cost/total amount of pending occasions etc...
     * @param view the fragment view.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewCountOfExpiredOccasions = view.findViewById(R.id.textViewCountOfExpiredOccasions);
        textViewSumOfItems = view.findViewById(R.id.textViewSumOfItems);
        textViewCountOfOccasions = view.findViewById(R.id.textViewCountOfOccasions);
        textViewCountOfPaidOccasions = view.findViewById(R.id.textViewCountOfPaidOccasions);

        Button locationsButton = view.findViewById(R.id.buttonHomeSeeAllLocations);

        //Open the maps fragment.
        locationsButton.setOnClickListener(v -> {
            coordinatesViewModel.setLocations(occasionModels);
            Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_allOccasionsMapFragment);
        });

        //Observe all the occasions.
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

        //Observe the active occasions.
        occasionViewModel.getActiveOccasions().observe(requireActivity(), items ->{
            int totalOccasions = items.size();
            animate(totalOccasions, textViewCountOfOccasions);
        });

        //Observe the expired occasions.
        occasionViewModel.getExpiredOccasions().observe(requireActivity(), items->{
            int totalExpired = items.size();
            animate(totalExpired, textViewCountOfExpiredOccasions);
        });

        //Observe all the paid occasions.
        occasionViewModel.getPaidOccasions().observe(requireActivity(), items ->{
            int totalPaid = items.size();
            animate(totalPaid, textViewCountOfPaidOccasions);
        });

        //Observe the total cost of the items.
        itemsViewModel.getTotalCost().observe(requireActivity(), items ->{
            if(items != null){
                animate(items, textViewSumOfItems);
            }
            else{
                textViewSumOfItems.setText("0.0");
            }
        });
    }

    /**
     * Animates our TextViews
     * @param end the count max value.
     * @param view the TextView to be animated.
     */
    private void animate(int end, TextView view)
    {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, end);
        animator.addUpdateListener(animation -> view.setText(String.valueOf(animation.getAnimatedValue())));
        animator.setDuration(600);
        animator.start();
    }
}