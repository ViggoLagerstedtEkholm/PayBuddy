package com.example.paybuddy.History;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Fragment that displays a list of history occasions.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class ListFragmentHistory extends Fragment {
    private OccasionViewModel occasionViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;

    public ListFragmentHistory(){
        // Required empty public constructor
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(requireActivity()).get(FilterSelectionViewModel.class);
        List<OccasionModel> occasionModels = new ArrayList<>();
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(occasionModels, this);
    }

    /**
     * This method will create the RecyclerView and fetch items to that RecyclerView.
     * @param inflater inflater for our view.
     * @param container view that contains other views.
     * @param savedInstanceState latest saved instance.
     * @return View
     */
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

    /**
     * This method is called when the fragment is created.
     * @param view the fragment view.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Apply filter.
        filterSelectionViewModel.getSelected().observe(requireActivity(), searchWord -> historyRecyclerViewAdapter.getFilter().filter(searchWord));
    }
}