package com.example.paybuddy.Occasions.List_of_items;

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

import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ItemInOccasionsFragment extends Fragment {
    private ItemsViewModel itemsViewModel;
    private int mColumnCount = 1;
    private MyItemInOccasionRecyclerViewAdapter myItemInOccasionRecyclerViewAdapter;

    public ItemInOccasionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemsViewModel = new ViewModelProvider(requireParentFragment()).get(ItemsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_occasion, container, false);

        myItemInOccasionRecyclerViewAdapter = new MyItemInOccasionRecyclerViewAdapter(new ArrayList<>(), itemsViewModel);

        itemsViewModel.getPendingItems().observe(getActivity(), itemModels -> {
            myItemInOccasionRecyclerViewAdapter.addItems(itemModels);
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
            recyclerView.setAdapter(myItemInOccasionRecyclerViewAdapter);
        }
        return view;
    }
}