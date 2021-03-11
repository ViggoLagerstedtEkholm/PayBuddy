package com.example.paybuddy.Occasions.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.paybuddy.Occasions.ViewModel.DateViewModel;

import java.text.DateFormat;
import java.util.Calendar;

/**
 *  This dialog shows the "Add date" function. Here the user can add a date.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final Calendar calendar = Calendar.getInstance();
    private DateViewModel dateViewModel;

    /**
     * Instantiate the ViewModel.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateViewModel = new ViewModelProvider(requireParentFragment()).get(DateViewModel.class);
    }

    /**
     * When we create the dialog we automatically select the YEAR/MONTH/DAY_OF_MONTH.
     * @param savedInstanceState latest saved instance.
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * When the user has set the date this method gets called. We get the selected date and send it with
     * our ViewModel.
     * @param view the datePicker.
     * @param year the year value.
     * @param month the month value.
     * @param dayOfMonth the dayOfMonth value.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

        dateViewModel.setDate(currentDateString);
    }
}
