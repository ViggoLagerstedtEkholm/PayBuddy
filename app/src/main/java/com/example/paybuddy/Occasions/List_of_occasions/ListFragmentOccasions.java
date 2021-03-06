package com.example.paybuddy.Occasions.List_of_occasions;

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

public class ListFragmentOccasions extends Fragment {
    private int mColumnCount = 1;
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;
    private LocationViewModel locationViewModel;
    private ActiveOccasionRecyclerViewAdapter activeOccasionRecyclerViewAdapter;

    public ListFragmentOccasions() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(getActivity()).get(FilterSelectionViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions_list, container, false);

        activeOccasionRecyclerViewAdapter = new ActiveOccasionRecyclerViewAdapter(new ArrayList<>(), this, occasionViewModel, itemsViewModel, locationViewModel);

        occasionViewModel.getActiveOccasions().observe(getViewLifecycleOwner(), new Observer<List<OccasionWithItems>>() {
            @Override
            public void onChanged(List<OccasionWithItems> occasionWithItems) {
                List<OccasionModel> occasionModels = new ArrayList<>();
                for(OccasionWithItems occasionModel : occasionWithItems){
                    OccasionModel aOccasionModel = occasionModel.occasionModel;
                    aOccasionModel.setItems(occasionModel.itemModelList);
                    aOccasionModel.setLocationModel(occasionModel.locationModel);

                    occasionModels.add(aOccasionModel);
                }
                activeOccasionRecyclerViewAdapter.addItems(occasionModels);
            }
        });

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(activeOccasionRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterSelectionViewModel.getSelected().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String searchWord) {
                activeOccasionRecyclerViewAdapter.getFilter().filter(searchWord);
            };
        });
    }
}