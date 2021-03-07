package com.example.paybuddy.Occasions.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;

import java.util.ArrayList;

public class DialogPreviewOccasion extends DialogFragment {
    private OccasionModel occasionModel;
    private RecyclerView recyclerView;
    private PreviewRecyclerViewAdapter recyclerViewAdapter;
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;
    private static final String ARG_COLUMN_COUNT = "column-count";

    public DialogPreviewOccasion(){}

    public DialogPreviewOccasion(OccasionModel occasionModel){
        this.occasionModel = occasionModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(getActivity()).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_preview_occasion, null);

        instantiate(view);

        TextView textViewPreviewTitle = (TextView) view.findViewById(R.id.textViewPreviewTitle);
        TextView textViewPreviewExpiringDate = (TextView) view.findViewById(R.id.textViewPreviewExpiringDate);
        TextView textViewPreviewPeople = (TextView) view.findViewById(R.id.textViewPreviewPeople);
        TextView textViewPreviewTotalCost = (TextView) view.findViewById(R.id.textViewPreviewTotalCost);

        textViewPreviewTotalCost.setText("0.0");

        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(this, cost -> {
            if(cost != null){
                textViewPreviewTotalCost.setText(Double.toString(cost.doubleValue()));
            }
        });

        itemsViewModel.getPeopleOccasion(occasionModel.getID()).observe(this, people -> {
            if(people != null){
                String personNames = "";
                if(people.size() <= 3){
                    for(String person : people){
                        Log.d("Person: ", person);
                        personNames += " [" + person + "] ";
                    }
                }
                else{
                    personNames = "More than 3 people...";
                }
                textViewPreviewPeople.setText(personNames);
            }else {
                textViewPreviewPeople.setText("None");
            }
        });

        itemsViewModel.getOccasionItems(occasionModel.getID()).observe(this, items ->{
            recyclerViewAdapter.addItems(items);
        });

        textViewPreviewTitle.setText(occasionModel.getDescription());
        textViewPreviewExpiringDate.setText(occasionModel.getDate());

        Button buttonBackFromPreview = (Button) view.findViewById(R.id.buttonBackFromPreview);
        buttonBackFromPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        return alertDialog;
    }

    private void instantiate(View view){
        recyclerView = view.findViewById(R.id.activity_main_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new PreviewRecyclerViewAdapter(new ArrayList<>(), occasionViewModel, itemsViewModel);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}