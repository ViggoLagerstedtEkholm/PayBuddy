package com.example.paybuddy.Expired;

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

import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class sets our RecyclerView up and adds items to it.
 * We use the interface Filterable to filter results in this RecyclerView.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class ListFragmentDuePayment extends Fragment {
    private TimesUpRecyclerViewAdapter timesUpRecyclerViewAdapter;
    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private ItemsViewModel itemsViewModel;
    private FilterSelectionViewModel filterSelectionViewModel;

    public ListFragmentDuePayment() {
        // Required empty public constructor
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(requireActivity()).get(OccasionViewModel.class);
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        itemsViewModel = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
        filterSelectionViewModel = new ViewModelProvider(requireActivity()).get(FilterSelectionViewModel.class);
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
        View view = inflater.inflate(R.layout.fragment_list_due_payment, container, false);

        //Create RecyclerView.
        timesUpRecyclerViewAdapter = new TimesUpRecyclerViewAdapter(new ArrayList<>(), occasionViewModel, itemsViewModel, locationViewModel, this);

        //Observe occasions fetched.
        occasionViewModel.getExpiredOccasions().observe(getViewLifecycleOwner(), occasionWithItems -> {
            List<OccasionModel> occasionModels = new ArrayList<>();
            //Fill our occasion list.
            for(OccasionWithItems occasionModel : occasionWithItems){
                OccasionModel aOccasionModel = occasionModel.occasionModel;
                aOccasionModel.setItems(occasionModel.itemModelList);
                aOccasionModel.setLocationModel(occasionModel.locationModel);

                occasionModels.add(aOccasionModel);
            }
            //Add the items to the recycler.
            timesUpRecyclerViewAdapter.addItems(occasionModels);
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(timesUpRecyclerViewAdapter);
        }

        return view;
    }

    /**
     * This method is called when the fragment is created.
     * @param view the fragment view.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Observe search query.
        filterSelectionViewModel.getSelected().observe(requireActivity(), searchWord -> timesUpRecyclerViewAdapter.getFilter().filter(searchWord));
    }
}