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

/**
 *  This dialog shows the "Make expired" function. Here the user can make a pending occasion expired.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class DialogMakeExpired extends DialogFragment {
    private OccasionViewModel occasionViewModel;
    private final OccasionModel occasionModel;

    //Receives the OccasionModel we want to expire.
    public DialogMakeExpired(OccasionModel occasionModel){
        this.occasionModel = occasionModel;
    }

    /**
     * Instantiate the ViewModel.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
    }

    /**
     * In this method we create the dialog that will query the user to expire an occasion.
     * @param savedInstanceState latest saved instance.
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_make_expired, null);

        Button makeExpired = view.findViewById(R.id.buttonConfirmExpire);
        Button cancelExpire = view.findViewById(R.id.buttonCancelExpire);

        cancelExpire.setOnClickListener(v -> dismiss());

        makeExpired.setOnClickListener(v -> {
            occasionModel.setExpired(true);
            occasionViewModel.update(occasionModel);
            dismiss();
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        return alertDialog;
    }
}
