package com.example.paybuddy.TimesUp.TimesUp.List;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentDuePayment extends Fragment {
    private int mColumnCount = 1;
    private TimesUpRecyclerViewAdapter timesUpRecyclerViewAdapter;
    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private ItemsViewModel itemsViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;
    private List<OccasionModel> occasionModels;

    public ListFragmentDuePayment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(getActivity()).get(OccasionViewModel.class);
        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        itemsViewModel = new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(getActivity()).get(FilterSelectionViewModel.class);
        occasionModels = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_due_payment, container, false);

        timesUpRecyclerViewAdapter = new TimesUpRecyclerViewAdapter(new ArrayList<>(), occasionViewModel, itemsViewModel, locationViewModel, this);

        occasionViewModel.getExpiredOccasions().observe(getViewLifecycleOwner(), occasionWithItems -> {
            List<OccasionModel> occasionModels = new ArrayList<>();
            for(OccasionWithItems occasionModel : occasionWithItems){
                OccasionModel aOccasionModel = occasionModel.occasionModel;
                aOccasionModel.setItems(occasionModel.itemModelList);
                aOccasionModel.setLocationModel(occasionModel.locationModel);

                occasionModels.add(aOccasionModel);
            }
            timesUpRecyclerViewAdapter.addItems(occasionModels);
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(timesUpRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterSelectionViewModel.getSelected().observe(getActivity(), searchWord -> timesUpRecyclerViewAdapter.getFilter().filter(searchWord));
    }
}