package com.example.paybuddy.Settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.R;
import com.example.paybuddy.Repositories.Repository;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

public class DialogDeleteExpiredConfirm extends DialogFragment {
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;
    private LocationViewModel locationViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(getActivity()).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_confirm, null);

        Button confirmDeleteOk = (Button) view.findViewById(R.id.buttonConfirmDelete);
        Button cancelDelete = (Button) view.findViewById(R.id.buttonCancelDelete);

        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirmDeleteOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationViewModel.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_EXPIRED);
                itemsViewModel.deleteAllItems(Repository.DELETE_TYPE.DELETE_ALL_EXPIRED);
                occasionViewModel.deleteAllExpired();
                Toast.makeText(getContext(), "Deleted all expired occasions!", Toast.LENGTH_SHORT).show();
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