package com.example.paybuddy.TimesUp.TimesUp.List;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.FilterViewModel;
import com.example.paybuddy.database.DatabaseHelper;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentDuePayment extends Fragment {

    private FilterViewModel filterViewModel;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public ListFragmentDuePayment() {

    }

    @SuppressWarnings("unused")
    public static ListFragmentDuePayment newInstance(int columnCount) {
        ListFragmentDuePayment fragment = new ListFragmentDuePayment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_due_payment, container, false);

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(databaseHelper.filterOccasion("NONE", DatabaseHelper.FILTER_TYPE.SEARCH_EXPIRED));

        filterViewModel.getSelected().observe(getViewLifecycleOwner(), word -> {
            List<OccasionModel> occasionModelArrayList = databaseHelper.filterOccasion(word, DatabaseHelper.FILTER_TYPE.SEARCH_ISPAID);
            myItemRecyclerViewAdapter.addItems(occasionModelArrayList);
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