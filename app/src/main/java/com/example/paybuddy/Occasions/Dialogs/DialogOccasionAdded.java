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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.paybuddy.Home.UpdateViewModel;
import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;

public class DialogOccasionAdded extends DialogFragment {
    private TextView textViewResultTotal;
    private TextView exipiringDate;
    private OccasionModel occasionModel;
    private View currentView;

    public DialogOccasionAdded(OccasionModel occasionModel, View currentView){
        this.occasionModel = occasionModel;
        this.currentView = currentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.success_add_occasion, null);
        textViewResultTotal = (TextView) view.findViewById(R.id.textViewResultTotal);
        exipiringDate = (TextView) view.findViewById(R.id.textViewResultExpiringDate);

        double totalPriceReturned = getTotalPrice();
        textViewResultTotal.setText(Double.toString(totalPriceReturned));
        exipiringDate.setText(occasionModel.getDate());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        final AlertDialog alertDialog= alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Button buttonOnResultOK = (Button) view.findViewById(R.id.buttonOnResultOK);

        buttonOnResultOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(currentView).navigate(R.id.action_occasionAddFragment_to_tabViewFragment);
                dismiss();
            }
        });

        return alertDialog;
    }

    private double getTotalPrice(){
        double price = 0;
//        for(ItemModel aModel : occasionModel.getItems()){
       //     price += aModel.getPrice();
        //}
        return price;
    }
}