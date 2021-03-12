package com.example.paybuddy.Settings;

import android.widget.EditText;
import java.util.List;

/**
 * The Validator class should validate user input.
 *
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class Validator {

    /**
     *  This method returns false if a EditText is empty.
     * @param editTextList list of EditText
     * @return boolean
     */
    public static boolean EditTextHasValues(List<EditText> editTextList){
        for(EditText editText : editTextList){
            if(editText.getText().toString().equals("")){
                return false;
            }
        }
        return true;
    }
}
