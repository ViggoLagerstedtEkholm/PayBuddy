package com.example.paybuddy.History.List;

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

import com.example.paybuddy.MVVM.RepositoryViewModel;
import com.example.paybuddy.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentHistory extends Fragment {

    private int mColumnCount = 1;
    private RepositoryViewModel repositoryViewModel;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public ListFragmentHistory() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositoryViewModel = new ViewModelProvider(requireActivity()).get(RepositoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);


        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new ArrayList<>());

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