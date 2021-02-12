package com.example.paybuddy.History.List;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.History.ViewModel.FilterViewModelHistory;
import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ListFragment extends Fragment {

    private int mColumnCount = 1;
    private FilterViewModelHistory filterViewModelHistory;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new ArrayList());
        filterViewModelHistory = new ViewModelProvider(requireActivity()).get(FilterViewModelHistory.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        filterViewModelHistory.getSelected().observe(getViewLifecycleOwner(), word -> {
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
}