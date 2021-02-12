package com.example.paybuddy.Occasions.list_of_items;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ItemInputFragment extends Fragment {

    public ItemInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_input, container, false);
    }
}