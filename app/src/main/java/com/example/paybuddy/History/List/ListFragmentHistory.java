package com.example.paybuddy.History.List;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;

import java.util.ArrayList;
import java.util.List;


public class ListFragmentHistory extends Fragment {
    private OccasionViewModel occasionViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;

    public ListFragmentHistory(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        ItemsViewModel itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        LocationViewModel locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(requireActivity()).get(FilterSelectionViewModel.class);
        List<OccasionModel> occasionModels = new ArrayList<>();
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(occasionModels, this, occasionViewModel, locationViewModel, itemsViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);

        occasionViewModel.getPaidOccasions().observe(requireActivity(), occasionWithItems -> {
            List<OccasionModel> occasionModels = new ArrayList<>();

            for(OccasionWithItems occasionModel : occasionWithItems){
                OccasionModel aOccasionModel = occasionModel.occasionModel;
                aOccasionModel.setItems(occasionModel.itemModelList);
                aOccasionModel.setLocationModel(occasionModel.locationModel);

                occasionModels.add(aOccasionModel);
            }

            historyRecyclerViewAdapter.addItems(occasionModels);
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(historyRecyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterSelectionViewModel.getSelected().observe(requireActivity(), searchWord -> historyRecyclerViewAdapter.getFilter().filter(searchWord));
    }
}