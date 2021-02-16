package com.example.paybuddy.Occasions.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.Occasions.ViewModel.PreviewViewModel;
import com.example.paybuddy.R;

public class DialogPreviewOccasion  extends DialogFragment {
    private OccasionModel occasionModel;
    private PreviewViewModel previewViewModel;

    public DialogPreviewOccasion(OccasionModel occasionModel){
        this.occasionModel = occasionModel;
        previewViewModel = new ViewModelProvider(this).get(PreviewViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_preview_occasion, null);

        previewViewModel.setItem(occasionModel);
        //TODO - CREATE LAYOUT FOR THIS DIALOG.

        TextView textViewPreviewTitle = (TextView) view.findViewById(R.id.textViewPreviewTitle);
        TextView textViewPreviewExpiringDate = (TextView) view.findViewById(R.id.textViewPreviewExpiringDate);
        TextView textViewPreviewTotalCost = (TextView) view.findViewById(R.id.textViewPreviewTotalCost);
        TextView textViewPreviewPeople = (TextView) view.findViewById(R.id.textViewPreviewPeople);

        double totalCost = 0.0;

//        for(ItemModel aModel : occasionModel.getItems()){
          //  totalCost += aModel.getPrice();
       // }

        textViewPreviewTitle.setText(occasionModel.getDescription());
        textViewPreviewExpiringDate.setText(occasionModel.getDate());
        textViewPreviewTotalCost.setText(Double.toString(totalCost));
        textViewPreviewPeople.setText("TODO");

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
}
