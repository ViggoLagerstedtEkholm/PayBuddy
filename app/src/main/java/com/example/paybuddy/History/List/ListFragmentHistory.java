package com.example.paybuddy.History.List;

import android.content.Context;
import android.os.Bundle;

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

import com.example.paybuddy.MVVM.OccasionViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentHistory extends Fragment {

    private int mColumnCount = 1;
    private OccasionViewModel occasionViewModel;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public ListFragmentHistory() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(requireActivity()).get(OccasionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);


        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new ArrayList<>());

        occasionViewModel.getOccasionsWithItems().observe(getViewLifecycleOwner(), new Observer<List<OccasionWithItems>>() {
            @Override
            public void onChanged(List<OccasionWithItems> occasionWithItems) {
                Log.d("History data received", "...");
                List<OccasionModel> occasionModels = new ArrayList<>();
                for(OccasionWithItems occasionModel : occasionWithItems){
                    OccasionModel aOccasionModel = occasionModel.occasionModel;
                    if(aOccasionModel.isPaid() && !aOccasionModel.isExpired()){
                        aOccasionModel.setItems(occasionModel.itemModelList);
                        occasionModels.add(aOccasionModel);
                    }
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
}