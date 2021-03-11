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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;

import java.util.ArrayList;

/**
 *  This dialog shows the "Make expired" function. Here the user can make a pending occasion expired.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class DialogPreviewOccasion extends DialogFragment {
    private OccasionModel occasionModel;
    private PreviewRecyclerViewAdapter recyclerViewAdapter;
    private ItemsViewModel itemsViewModel;

    //Receives the OccasionModel that we want to preview.
    public DialogPreviewOccasion(OccasionModel occasionModel){
        this.occasionModel = occasionModel;
    }

    /**
     * Instantiate the ViewModel.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsViewModel = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
    }

    /**
     * In this method we create the dialog that will show the occasion content.
     * @param savedInstanceState latest saved instance.
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_preview_occasion, null);

        instantiate(view);

        TextView textViewPreviewTitle = view.findViewById(R.id.textViewPreviewTitle);
        TextView textViewPreviewExpiringDate = view.findViewById(R.id.textViewPreviewExpiringDate);
        TextView textViewPreviewPeople = view.findViewById(R.id.textViewPreviewPeople);
        TextView textViewPreviewTotalCost = view.findViewById(R.id.textViewPreviewTotalCost);

        textViewPreviewTotalCost.setText("0.0");

        //Get occasion cost for this occasion.
        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(this, cost -> {
            if(cost != null){
                textViewPreviewTotalCost.setText(String.valueOf(cost));
            }
        });

        //Get the people in this occasion.
        itemsViewModel.getPeopleOccasion(occasionModel.getID()).observe(this, people -> {
            if(people != null){
                StringBuilder personNames = new StringBuilder();
                if(people.size() <= 3){
                    for(String person : people){
                        Log.d("Person: ", person);
                        personNames.append(" [").append(person).append("] ");
                    }
                }
                else{
                    personNames = new StringBuilder("More than 3 people...");
                }
                textViewPreviewPeople.setText(personNames.toString());
            }else {
                textViewPreviewPeople.setText("None");
            }
        });

        //Get occasion items for this occasion.
        itemsViewModel.getOccasionItems(occasionModel.getID()).observe(this, items -> recyclerViewAdapter.addItems(items));

        textViewPreviewTitle.setText(occasionModel.getDescription());
        textViewPreviewExpiringDate.setText(occasionModel.getDate());

        Button buttonBackFromPreview = view.findViewById(R.id.buttonBackFromPreview);
        buttonBackFromPreview.setOnClickListener(v -> dismiss());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        return alertDialog;
    }

    /**
     * Here we setup the RecyclerView.
     * @param view the fragment view
     */
    private void instantiate(View view){
        RecyclerView recyclerView = view.findViewById(R.id.activity_main_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new PreviewRecyclerViewAdapter(new ArrayList<>(), itemsViewModel);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}