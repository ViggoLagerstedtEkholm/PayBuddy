package com.example.paybuddy.Occasions.List_of_occasions;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.ItemModel;
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
public class ListFragmentOccasions extends Fragment {
    private int mColumnCount = 1;
    private RepositoryViewModel repositoryViewModel;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());

    public ListFragmentOccasions() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositoryViewModel = new ViewModelProvider(requireActivity()).get(RepositoryViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions_list, container, false);

        repositoryViewModel.init(getContext());
        repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_NOTPAID).observe(getViewLifecycleOwner(), list -> {
                 Log.d("UPDATING OCCASIONS", String.valueOf(list.size()));
                for(OccasionModel occasionModel : list){
                Log.d("occasion items", occasionModel.getID() + " : " + occasionModel.getDescription());
            }
                myItemRecyclerViewAdapter.notifyDataSetChanged();
        });

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(repositoryViewModel.getOccasions(FILTER_TYPE.SEARCH_NOTPAID).getValue(), this, repositoryViewModel);

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