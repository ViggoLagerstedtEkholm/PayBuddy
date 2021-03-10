package com.example.paybuddy.Search.SearchBoxes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterPhoneHistoryViewModel;

/**
 * This fragment is responsible for
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class SearchBoxHistoryCallFragment extends Fragment {
    private FilterPhoneHistoryViewModel filterPhoneHistoryViewModel;

    public SearchBoxHistoryCallFragment() {
        // Required empty public constructor
    }

    /**
     * This method is called when we create this fragment.
     * We create our ViewModel and scope it to our activity.
     * This ViewModel will be sending the query String to the Phone filter fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterPhoneHistoryViewModel = new ViewModelProvider(getActivity()).get(FilterPhoneHistoryViewModel.class);
    }

    /**
     * This method inflates our view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_box, container, false);
    }

    /**
     * This method gets the SearchView from the fragment and adds onQueryListener to set the ViewModel
     * Query for our search.
     * @param view the inflated view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.search);

        //Add a listener to the SearchView.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //Called when we change SearchView text.
            @Override
            public boolean onQueryTextChange(String newText) {
                filterPhoneHistoryViewModel.select(newText);
                return false;
            }
        });
    }
}