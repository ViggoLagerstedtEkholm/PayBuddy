package com.example.paybuddy.Occasions.List_of_items;

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

import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;

import java.util.ArrayList;

/**
 * This is the Fragment that displays a list of items in a particular occasion.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class ItemInOccasionsFragment extends Fragment {
    private ItemsViewModel itemsViewModel;
    private MyItemInOccasionRecyclerViewAdapter myItemInOccasionRecyclerViewAdapter;

    public ItemInOccasionsFragment() {
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemsViewModel = new ViewModelProvider(requireParentFragment()).get(ItemsViewModel.class);
    }

    /**
     * This method is called when the fragment is created.
     * @param view the fragment view.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_list_occasion, container, false);

        myItemInOccasionRecyclerViewAdapter = new MyItemInOccasionRecyclerViewAdapter(new ArrayList<>(), itemsViewModel);

        itemsViewModel.getPendingItems().observe(requireActivity(), itemModels -> myItemInOccasionRecyclerViewAdapter.addItems(itemModels));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(myItemInOccasionRecyclerViewAdapter);
        }
        return view;
    }
}