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
import com.example.paybuddy.Search.SearchViewModels.FilterContactViewModel;
import com.example.paybuddy.Search.SearchViewModels.FilterSelectionViewModel;

public class SearchBoxContactFragment extends Fragment {
    private FilterContactViewModel filterContactViewModel;
    private EditText searchWord;

    public SearchBoxContactFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterContactViewModel = new ViewModelProvider(getActivity()).get(FilterContactViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_box, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchWord = (EditText) view.findViewById(R.id.txfItemName);

        SearchView searchView = (SearchView) view.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContactViewModel.select(newText);
                return false;
            }
        });
    }
}