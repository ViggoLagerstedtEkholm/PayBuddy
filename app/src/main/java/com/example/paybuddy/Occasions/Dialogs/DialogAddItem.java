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

public class DialogAddItem extends DialogFragment {
    private ItemsViewModel itemsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_item_input, null);

        List<EditText> textList = new ArrayList<>();

        Button buttonAdd = (Button) view.findViewById(R.id.buttonAddItemToList);
        EditText txfItemName = (EditText) view.findViewById(R.id.txfItemName);
        EditText txfItemPrice = (EditText) view.findViewById(R.id.txfItemPrice);
        EditText txfItemQuantity = (EditText) view.findViewById(R.id.txfItemQuantity);
        EditText txfItemPersonName = (EditText) view.findViewById(R.id.txfItemPersonName);

        textList.add(txfItemName);
        textList.add(txfItemPrice);
        textList.add(txfItemQuantity);
        textList.add(txfItemPersonName);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
