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

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Repositories.RepositoryViewModel;
import com.example.paybuddy.Search.FilterViewModel;
import com.example.paybuddy.R;
import com.example.paybuddy.database.DatabaseHelper;
import com.example.paybuddy.database.FILTER_TYPE;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentHistory extends Fragment {

    private int mColumnCount = 1;
    private FilterViewModel filterViewModel;
    private RepositoryViewModel repositoryViewModel;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
    public ListFragmentHistory() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        repositoryViewModel = new ViewModelProvider(requireActivity()).get(RepositoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);
        repositoryViewModel.init(getContext());

        repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_ISPAID).observe(getViewLifecycleOwner(), list ->{
            Log.d("UPDATING HISTORY", String.valueOf(list.size()));
            for(OccasionModel occasionModel : list){
                Log.d("History items", occasionModel.getID() + " : " + occasionModel.getDescription());
            }
            myItemRecyclerViewAdapter.notifyDataSetChanged();
        });

        filterViewModel.getSelected().observe(getViewLifecycleOwner(), word -> {
            List<OccasionModel> occasionModelArrayList = repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_ISPAID).getValue();
            myItemRecyclerViewAdapter.addItems(occasionModelArrayList);
        });

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_ISPAID).getValue());

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