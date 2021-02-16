package com.example.paybuddy.Occasions.List_of_occasions;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.paybuddy.MVVM.RepositoryViewModel;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;

import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ListFragmentOccasions extends Fragment {
    private int mColumnCount = 1;
    private RepositoryViewModel repositoryViewModel;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public ListFragmentOccasions() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositoryViewModel = new ViewModelProvider(getActivity()).get(RepositoryViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions_list, container, false);

        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new ArrayList<>(), this, repositoryViewModel);

        //repositoryViewModel.getAllOccasions().observe(getViewLifecycleOwner(), new Observer<List<OccasionModel>>(){
        //    @Override
        //    public void onChanged(List<OccasionModel> occasionModels) {
        //        Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
        //        Log.d("LIVEDATA RECEIVED", "JUST OCCASIONS!");
        //        myItemRecyclerViewAdapter.addItems(occasionModels);
        //    }
       // });

        repositoryViewModel.getOccasionsWithItems().observe(getViewLifecycleOwner(), new Observer<List<OccasionWithItems>>() {
            @Override
            public void onChanged(List<OccasionWithItems> occasionWithItems) {
                List<OccasionModel> occasionModels = new ArrayList<>();
                Log.d("LIVEDATA RECEIVED", "OCCASIONS WITH ITEMS!");
                for(OccasionWithItems occasionWithItems1 : occasionWithItems){
                    OccasionModel occasionModel = occasionWithItems1.occasionModel;
                    List<ItemModel> itemModels = occasionWithItems1.itemModelList;

                    occasionModel.setItems(itemModels);
                    occasionModels.add(occasionModel);

                    Log.d("Occasion: " , occasionModel.getDescription());
                    Log.d("Items: " , "...");

                    for(ItemModel itemModel : occasionModel.getItems()){
                        Log.d("Description: " , itemModel.getDescription());
                    }
                }

                myItemRecyclerViewAdapter.addItems(occasionModels);

            }
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