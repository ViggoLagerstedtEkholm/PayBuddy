package com.example.paybuddy;

import android.util.Log;
import android.widget.EditText;

import java.util.List;

public class Validator {
    public static boolean EditTextHasValues(List<EditText> editTextList){
        for(EditText editText : editTextList){
            if(editText.getText().toString().equals("")){
                return false;
            }
        }
        return true;
    }
}
