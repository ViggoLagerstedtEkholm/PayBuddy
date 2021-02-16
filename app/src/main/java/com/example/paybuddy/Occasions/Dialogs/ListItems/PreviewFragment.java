package com.example.paybuddy.Occasions.Dialogs.ListItems;

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

import com.example.paybuddy.Occasions.ViewModel.PreviewViewModel;
import com.example.paybuddy.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class PreviewFragment extends Fragment {
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private PreviewViewModel previewViewModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PreviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        previewViewModel = new ViewModelProvider(getParentFragment()).get(PreviewViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_occasion, container, false);

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new ArrayList<>());

        previewViewModel.getItem().observe(getViewLifecycleOwner(), items -> {
            myItemRecyclerViewAdapter.addItems(items.getItems());
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