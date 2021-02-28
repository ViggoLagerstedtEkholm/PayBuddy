package com.example.paybuddy.Occasions.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

public class DialogMakeExpired extends DialogFragment {
    private OccasionViewModel occasionViewModel;
    private OccasionModel occasionModel;

    public DialogMakeExpired(OccasionModel occasionModel){
        this.occasionModel = occasionModel;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_make_expired, null);

        Button makeExpired = (Button) view.findViewById(R.id.buttonConfirmExpire);
        Button cancelExpire = (Button) view.findViewById(R.id.buttonCancelExpire);

        cancelExpire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        makeExpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                occasionModel.setExpired(true);
                occasionViewModel.update(occasionModel);
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
