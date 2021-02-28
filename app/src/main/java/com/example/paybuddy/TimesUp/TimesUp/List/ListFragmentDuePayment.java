package com.example.paybuddy.TimesUp.TimesUp.List;

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

import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Search.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentDuePayment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private ItemsViewModel itemsViewModel;
    private FilterViewModel filterViewModel;
    private List<OccasionModel> occasionModels;

    public ListFragmentDuePayment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        filterViewModel = new ViewModelProvider(getActivity()).get(FilterViewModel.class);
        occasionModels = new ArrayList<>();

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(occasionModels, occasionViewModel, itemsViewModel, locationViewModel, getContext());

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_due_payment, container, false);
        occasionViewModel.getExpiredOccasions().observe(getViewLifecycleOwner(), new Observer<List<OccasionWithItems>>() {
            @Override
            public void onChanged(List<OccasionWithItems> occasionWithItems) {
                Log.d("Due data received", "..." + String.valueOf(occasionWithItems.size()));
                List<OccasionModel> occasionModels = new ArrayList<>();
                for(OccasionWithItems occasionModel : occasionWithItems){
                    OccasionModel aOccasionModel = occasionModel.occasionModel;
                    aOccasionModel.setItems(occasionModel.itemModelList);
                    Log.d("Due data :", aOccasionModel.getDescription());

                    occasionModels.add(aOccasionModel);
                }
                myItemRecyclerViewAdapter.addItems(occasionModels);
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