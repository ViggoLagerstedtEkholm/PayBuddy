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

import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;

import java.util.ArrayList;
import java.util.List;


public class ListFragmentHistory extends Fragment {

    private int mColumnCount = 1;
    private OccasionViewModel occasionViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;
    private HistoryRecyclerViewAdapter historyRecyclerViewAdapter;
    private List<OccasionModel> occasionModels;

    public ListFragmentHistory() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(getActivity()).get(FilterSelectionViewModel.class);
        occasionModels = new ArrayList<>();
        historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(getContext(), occasionModels, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);

        occasionViewModel.getPaidOccasions().observe(getActivity(), new Observer<List<OccasionWithItems>>() {
            @Override
            public void onChanged(List<OccasionWithItems> occasionWithItems) {
                Log.d("History data received", "..." + String.valueOf(occasionWithItems.size()));
                List<OccasionModel> occasionModels = new ArrayList<>();

                for(OccasionWithItems occasionModel : occasionWithItems){
                    OccasionModel aOccasionModel = occasionModel.occasionModel;
                    aOccasionModel.setItems(occasionModel.itemModelList);

                    occasionModels.add(aOccasionModel);
                }

                historyRecyclerViewAdapter.addItems(occasionModels);
            }
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
            recyclerView.setAdapter(historyRecyclerViewAdapter);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterSelectionViewModel.getSelected().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String searchWord) {
                historyRecyclerViewAdapter.getFilter().filter(searchWord);
            };
        });
    }
}