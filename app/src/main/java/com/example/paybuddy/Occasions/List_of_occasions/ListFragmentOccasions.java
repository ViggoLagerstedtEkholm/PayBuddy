package com.example.paybuddy.Occasions.List_of_occasions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paybuddy.History.ViewModel.FilterViewModelHistory;
import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.ViewModel.FilterViewModelOccasion;
import com.example.paybuddy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentOccasions extends Fragment {
    private int mColumnCount = 1;
    private FilterViewModelOccasion filterViewModelOccasion;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<ItemModel> items;
    public ListFragmentOccasions() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterViewModelOccasion = new ViewModelProvider(requireActivity()).get(FilterViewModelOccasion.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions_list, container, false);

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(getAllOccasions(), this);

        filterViewModelOccasion.getSelected().observe(getViewLifecycleOwner(), word -> {
            String searchWord = word;
            myItemRecyclerViewAdapter.addItems(MainActivity.databaseHelper.filterOccasion(searchWord));
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

    private List<OccasionModel> getAllOccasions(){
        return MainActivity.databaseHelper.getAllOccasions();
    }
}