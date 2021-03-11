package com.example.paybuddy.Occasions.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Validator;
import com.example.paybuddy.Viewmodels.ItemsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *  This dialog shows the "Add item" function. Here the user can add a item.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class DialogAddItem extends DialogFragment {
    private ItemsViewModel itemsViewModel;
    private EditText txfItemName;
    private EditText txfItemPrice;
    private EditText txfItemQuantity;
    private EditText txfItemPersonName;
    private List<EditText> textList;

    /**
     * Instantiate the ViewModel.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
    }

    /**
     * This method is called when the view is created.
     * We fetch the user inputs and check if there is any input in the EditTexts before adding it to the database.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_item_input, null);

        textList = new ArrayList<>();

        Button buttonAdd = view.findViewById(R.id.buttonAddItemToList);
        txfItemName = view.findViewById(R.id.txfItemName);
        txfItemPrice = view.findViewById(R.id.txfItemPrice);
        txfItemQuantity = view.findViewById(R.id.txfItemQuantity);
        txfItemPersonName = view.findViewById(R.id.txfItemPersonName);

        textList.add(txfItemName);
        textList.add(txfItemPrice);
        textList.add(txfItemQuantity);
        textList.add(txfItemPersonName);

        buttonAdd.setOnClickListener(v -> {
            //Check all fields...
            if(Validator.EditTextHasValues(textList))
            {
                String title =  txfItemName.getText().toString();
                double price = Double.parseDouble(String.valueOf(txfItemPrice.getText()));
                int quantity = Integer.parseInt(String.valueOf(txfItemQuantity.getText()));
                String name = txfItemPersonName.getText().toString();

                ItemModel itemModel = new ItemModel(price, title, quantity, name);
                itemModel.setOccasionID(-1);
                itemsViewModel.insert(itemModel);

                dismiss();
            }
            else{
                Toast.makeText(getContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
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
