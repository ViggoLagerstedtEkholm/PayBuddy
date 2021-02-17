package com.example.paybuddy.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.loader.app.LoaderManager;

import com.example.paybuddy.R;

public class LoadingDialog {
    private Activity activty;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activty){
        this.activty = activty;
    }

    void startLoading(){
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(activty);

        LayoutInflater layoutInflater = activty.getLayoutInflater();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aBuilder.setView(layoutInflater.inflate(R.layout.fragment_loading, null));
        aBuilder.setCancelable(true);

        alertDialog = aBuilder.create();
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();
    }
}
