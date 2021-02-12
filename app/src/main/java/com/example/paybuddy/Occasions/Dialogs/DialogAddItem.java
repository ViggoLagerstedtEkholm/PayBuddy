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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.R;

public class DialogAddItem extends DialogFragment {
    private TextView totalPrice;
    private TextView exipiringDate;
    private OccasionModel occasionModel;
    private InputToItemListViewModel inputToItemListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputToItemListViewModel = new ViewModelProvider(requireActivity()).get(InputToItemListViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_item_input, null);

        Button buttonAdd = (Button) view.findViewById(R.id.buttonAddItemToList);
        TextView txfItemName = (TextView) view.findViewById(R.id.txfItemName);
        TextView txfItemPrice = (TextView) view.findViewById(R.id.txfItemPrice);
        TextView txfItemQuantity = (TextView) view.findViewById(R.id.txfItemQuantity);
        TextView txfItemPersonName = (TextView) view.findViewById(R.id.txfItemPersonName);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txfItemName.length() > 0 && txfItemName.length() > 0 && txfItemPrice.length() > 0){
                    String title =  txfItemName.getText().toString();
                    double price = Double.parseDouble(String.valueOf(txfItemPrice.getText()));
                    int quantity = Integer.parseInt(String.valueOf(txfItemQuantity.getText()));
                    String names = txfItemPersonName.getText().toString();
                    ItemModel itemModel = new ItemModel(-1, price, title, quantity);

                    inputToItemListViewModel.setItem(itemModel); // add item to data.

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("dialog destroy", "---");

        inputToItemListViewModel.getItem().removeObservers(this);
    }
}
