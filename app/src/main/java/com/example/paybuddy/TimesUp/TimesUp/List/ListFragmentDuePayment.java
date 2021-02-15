package com.example.paybuddy.TimesUp.TimesUp.List;

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

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Repositories.RepositoryViewModel;
import com.example.paybuddy.Search.FilterViewModel;
import com.example.paybuddy.database.DatabaseHelper;
import com.example.paybuddy.database.FILTER_TYPE;

import java.util.ArrayList;
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
    private RepositoryViewModel repositoryViewModel;

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
        repositoryViewModel = new ViewModelProvider(requireActivity()).get(RepositoryViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_due_payment, container, false);

        repositoryViewModel.init(getContext());

        repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_EXPIRED).observe(getViewLifecycleOwner(), new Observer<List<OccasionModel>>() {
            @Override
            public void onChanged(List<OccasionModel> occasionModels) {
                Log.d("EXPIRED HISTORY", "...");

                myItemRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_EXPIRED).getValue());

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