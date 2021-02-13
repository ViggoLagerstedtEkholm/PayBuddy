package com.example.paybuddy.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.R;

public class SearchBoxFragment extends Fragment implements View.OnClickListener {
    private FilterViewModel filterViewModel;
    private Button buttonSearch;
    private EditText searchWord;

    public SearchBoxFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_box, container, false);
    }

    @Override
    public void onClick(View view) {
        filterViewModel.select(searchWord.getText().toString());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
        searchWord = (EditText) view.findViewById(R.id.txfItemName);

        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterViewModel.select(searchWord.getText().toString());
            }
        });
    }
}