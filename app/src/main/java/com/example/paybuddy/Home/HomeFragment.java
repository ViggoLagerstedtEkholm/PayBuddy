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
        itemsViewModel = new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
        occasionViewModel = new ViewModelProvider(getActivity()).get(OccasionViewModel.class);
        coordinatesViewModel = new ViewModelProvider(getActivity()).get(CoordinatesViewModel.class);
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

        textViewCountOfExpiredOccasions = (TextView) view.findViewById(R.id.textViewCountOfExpiredOccasions);
        textViewSumOfItems = (TextView) view.findViewById(R.id.textViewSumOfItems);
        textViewCountOfOccasions = (TextView) view.findViewById(R.id.textViewCountOfOccasions);
        textViewCountOfPaidOccasions = (TextView) view.findViewById(R.id.textViewCountOfPaidOccasions);

        Button locationsButton = (Button) view.findViewById(R.id.buttonHomeSeeAllLocations);
        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordinatesViewModel.setLocations(occasionModels);
                Navigation.findNavController(view).navigate(R.id.action_tabViewFragment_to_allOccasionsMapFragment);
            }
        });

        occasionViewModel.getAllOccasions().observe(getActivity(), occasionWithItems -> {
            List<OccasionModel> occasionModels = new ArrayList<>();

            for(OccasionWithItems occasionModel : occasionWithItems){
                OccasionModel aOccasionModel = occasionModel.occasionModel;
                aOccasionModel.setItems(occasionModel.itemModelList);
                aOccasionModel.setLocationModel(occasionModel.locationModel);

                occasionModels.add(aOccasionModel);
            }
            this.occasionModels = occasionModels;
        });

        occasionViewModel.getActiveOccasions().observe(getActivity(), items ->{
            int totalOccasions = items.size();
            animate(0, totalOccasions, textViewCountOfOccasions);
        });

        occasionViewModel.getExpiredOccasions().observe(getActivity(), items->{
            int totalExpired = items.size();
            animate(0, totalExpired, textViewCountOfExpiredOccasions);
        });

        occasionViewModel.getPaidOccasions().observe(getActivity(), items ->{
            int totalPaid = items.size();
            animate(0, totalPaid, textViewCountOfPaidOccasions);
        });

        itemsViewModel.getTotalCost().observe(getActivity(), items ->{
            if(items != null){
                animate(0, items.intValue(), textViewSumOfItems);
            }
            else{
                textViewSumOfItems.setText("0.0");
            }
        });
    }

    private void animate(int start, int end, TextView view)
    {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setDuration(600);
        animator.start();
    }
}