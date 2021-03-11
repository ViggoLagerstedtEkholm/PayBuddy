package com.example.paybuddy.Occasions.List_of_occasions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *  This fragment shows the "Active occasions".
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class ListFragmentOccasions extends Fragment {
    private int mColumnCount = 1;
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;
    private LocationViewModel locationViewModel;
    private ActiveOccasionRecyclerViewAdapter activeOccasionRecyclerViewAdapter;

    public ListFragmentOccasions() {
        // Required empty public constructor
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(getActivity()).get(FilterSelectionViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    }

    /**
     * This method will create the RecyclerView and fetch items to that RecyclerView.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_occasions_list, container, false);

        //Create the adapter.
        activeOccasionRecyclerViewAdapter = new ActiveOccasionRecyclerViewAdapter(new ArrayList<>(), this, occasionViewModel, itemsViewModel, locationViewModel);

        //Observe occasions fetched.
        occasionViewModel.getActiveOccasions().observe(getViewLifecycleOwner(), occasionWithItems -> {
            List<OccasionModel> occasionModels = new ArrayList<>();
            //Fill our occasion list.
            for(OccasionWithItems occasionModel : occasionWithItems){
                OccasionModel aOccasionModel = occasionModel.occasionModel;
                aOccasionModel.setItems(occasionModel.itemModelList);
                aOccasionModel.setLocationModel(occasionModel.locationModel);

                occasionModels.add(aOccasionModel);
            }
            //Add the items to the recycler.
            activeOccasionRecyclerViewAdapter.addItems(occasionModels);
        });

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(activeOccasionRecyclerViewAdapter);
        }

        return view;
    }

    /**
     * This method is called when the fragment is created.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Observe search query.
        filterSelectionViewModel.getSelected().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String searchWord) {
                activeOccasionRecyclerViewAdapter.getFilter().filter(searchWord);
            };
        });
    }
}