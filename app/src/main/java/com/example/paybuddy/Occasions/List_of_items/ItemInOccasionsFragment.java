package com.example.paybuddy.Occasions.List_of_items;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Occasions.ViewModel.CompleteListViewModel;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.Occasions.ViewModel.PreviewViewModel;
import com.example.paybuddy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemInOccasionsFragment extends Fragment {
    private InputToItemListViewModel inputToItemListViewModel;
    private CompleteListViewModel completeListViewModel;
    private PreviewViewModel previewViewModel;
    private int mColumnCount = 1;
    private MyItemInOccasionRecyclerViewAdapter myItemInOccasionRecyclerViewAdapter;

    public ItemInOccasionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputToItemListViewModel = new ViewModelProvider(requireParentFragment()).get(InputToItemListViewModel.class);
        completeListViewModel = new ViewModelProvider(requireParentFragment()).get(CompleteListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_occasion, container, false);

        myItemInOccasionRecyclerViewAdapter = new MyItemInOccasionRecyclerViewAdapter(new ArrayList<>(), completeListViewModel);

        inputToItemListViewModel.getItem().observe(getViewLifecycleOwner(), itemModelSingleEventViewModel -> {
            myItemInOccasionRecyclerViewAdapter.addItemToList(itemModelSingleEventViewModel);
        });

        previewViewModel.getItem().observe(getViewLifecycleOwner(), item ->{
            List<ItemModel> items = item.getItems();
            myItemInOccasionRecyclerViewAdapter.addItems(items);
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