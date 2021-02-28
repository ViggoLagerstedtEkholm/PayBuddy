package com.example.paybuddy.Occasions.List_of_occasions;

import android.content.Context;
import android.location.Location;
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
import com.example.paybuddy.Search.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentOccasions extends Fragment {
    private int mColumnCount = 1;
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;
    private FilterViewModel filterViewModel;
    private LocationViewModel locationViewModel;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public ListFragmentOccasions() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        filterViewModel = new ViewModelProvider(getActivity()).get(FilterViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions_list, container, false);

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new ArrayList<>(), this, occasionViewModel, itemsViewModel, locationViewModel);

        occasionViewModel.getActiveOccasions().observe(getViewLifecycleOwner(), new Observer<List<OccasionWithItems>>() {
            @Override
            public void onChanged(List<OccasionWithItems> occasionWithItems) {
                Log.d("Occasion data received", "..." + String.valueOf(occasionWithItems.size()));

                List<OccasionModel> occasionModels = new ArrayList<>();
                for(OccasionWithItems occasionModel : occasionWithItems){
                    OccasionModel aOccasionModel = occasionModel.occasionModel;
                    aOccasionModel.setItems(occasionModel.itemModelList);
                    aOccasionModel.setLocationModel(occasionModel.locationModel);

                    occasionModels.add(aOccasionModel);
                }
                myItemRecyclerViewAdapter.addItems(occasionModels);
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

            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterViewModel.getSelected().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String searchWord) {
                myItemRecyclerViewAdapter.getFilter().filter(searchWord);
            };
        });
    }
}