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

/**
 *  This fragment displays a dialog that query the user for input.
 *  If the user accepts the dialog query the selected occasions will be removed.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class DeleteDialog extends DialogFragment {
    private OccasionViewModel occasionViewModel;
    private ItemsViewModel itemsViewModel;
    private LocationViewModel locationViewModel;

    private Repository.DELETE_TYPE delete_type;

    //The constructor takes the DELETE_TYPE as an argument.
    public DeleteDialog(Repository.DELETE_TYPE DELETE_TYPE){
        this.delete_type = DELETE_TYPE;
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(getActivity()).get(OccasionViewModel.class);
        itemsViewModel = new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
    }

    /**
     * Creates the dialog view.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_confirm, null);

        Button confirmDeleteOk = view.findViewById(R.id.buttonConfirmDelete);
        Button cancelDelete = view.findViewById(R.id.buttonCancelDelete);

        //Closes the dialog.
        cancelDelete.setOnClickListener(v -> dismiss());

        //If the user wants to delete, delete whatever the user clicked.
        confirmDeleteOk.setOnClickListener(v -> {
            switch(delete_type){
                case DELETE_ALL_EXPIRED: deleteAllExpired();
                    break;
                case DELETE_ALL_UNPAID: deleteUnPaidOccasions();
                    break;
                case DELETE_ALL_HISTORY: deleteHistory();
                    break;
                case DELETE_ALL: deleteAll();
                    break;
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        return alertDialog;
    }


    /**
     * Delete all expired occasions.
     * @return void
     */
    private void deleteAllExpired(){
        locationViewModel.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_EXPIRED);
        itemsViewModel.deleteAllItems(Repository.DELETE_TYPE.DELETE_ALL_EXPIRED);
        occasionViewModel.deleteAllExpired();
        Toast.makeText(getContext(), "Deleted all expired occasions!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    /**
     * Delete all occasions (pending, history, expired).
     * @return void
     */
    private void deleteAll(){
        locationViewModel.deleteAll(Repository.DELETE_TYPE.DELETE_ALL);
        itemsViewModel.deleteAllItems(Repository.DELETE_TYPE.DELETE_ALL);
        occasionViewModel.deleteAll();
        Toast.makeText(getContext(), "Deleted all data!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    /**
     * Delete all history occasions.
     * @return void
     */
    private void deleteHistory(){
        locationViewModel.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_HISTORY);
        itemsViewModel.deleteAllItems(Repository.DELETE_TYPE.DELETE_ALL_HISTORY);
        occasionViewModel.deleteAllHistory();
        Toast.makeText(getContext(), "Deleted all history!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    /**
     * Delete all pending occasions.
     * @return void
     */
    private void deleteUnPaidOccasions(){
        locationViewModel.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_UNPAID);
        itemsViewModel.deleteAllItems(Repository.DELETE_TYPE.DELETE_ALL_UNPAID);
        occasionViewModel.deleteAllUnpaid();
        Toast.makeText(getContext(), "Deleted all unpaid occasions!", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
