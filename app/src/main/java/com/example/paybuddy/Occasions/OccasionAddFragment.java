package com.example.paybuddy.Occasions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogAddItem;
import com.example.paybuddy.Occasions.Dialogs.DialogAddLocation;
import com.example.paybuddy.Occasions.Dialogs.DialogDatePicker;
import com.example.paybuddy.Occasions.ViewModel.DateViewModel;
import com.example.paybuddy.Occasions.ViewModel.LocationViewModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;

import com.example.paybuddy.Viewmodels.OccasionViewModel;
import com.example.paybuddy.Validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class OccasionAddFragment extends Fragment implements View.OnClickListener{
    private LocationModel location;
    private EditText title;
    private View currentView;

    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private DateViewModel dateViewModel;
    private ItemsViewModel itemsViewModel;

    private List<EditText> editTexts = new ArrayList<>();
    private String selectedDate;
    private TextView textDateDisplay;
    private CheckBox checkBoxAddCalendar;

    public OccasionAddFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.currentView = view;
        instantiate(view);
    }

    private void instantiate(View view) {
        checkBoxAddCalendar = (CheckBox) view.findViewById(R.id.checkBoxAddCalendar);
        title = (EditText) view.findViewById(R.id.FieldTitle);
        textDateDisplay = (TextView) view.findViewById(R.id.textDateDisplay);

        editTexts.add(title);

        TextView locationAdded = (TextView) view.findViewById(R.id.textAddedLocation);
        TextView textTotalItems = (TextView) view.findViewById(R.id.textTotalItemsLabel);

        Button buttonSave = (Button) view.findViewById(R.id.buttonEnter);
        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        Button buttonAddItems = (Button) view.findViewById(R.id.buttonAddItems);
        Button buttonEnterLocation = (Button) view.findViewById(R.id.buttonEnterLocation);
        Button buttonChooseDate = (Button) view.findViewById(R.id.buttonPickADate);
        Button buttonClearPendingItems = (Button) view.findViewById(R.id.buttonClearPendingItems);

        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonAddItems.setOnClickListener(this);
        buttonEnterLocation.setOnClickListener(this);
        buttonChooseDate.setOnClickListener(this);

        buttonClearPendingItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsViewModel.deletePendingItems();
            }
        });

        locationViewModel.getLocation().observe(getViewLifecycleOwner(), locationModel -> {
            this.location = locationModel;
            locationAdded.setText(location.getAdress());
        });

        dateViewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            this.selectedDate = date;
            textDateDisplay.setText(date);
        });

        itemsViewModel.getPendingItems().observe(getViewLifecycleOwner(), itemModels -> {
            int size = itemModels.size();
            textTotalItems.setText(String.valueOf(size));
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonEnter:
                if(Validator.EditTextHasValues(editTexts) && location != null && selectedDate != "" && itemsViewModel.getPendingItems().getValue().size() > 0)
                {
                    String occasionTitle = title.getText().toString();
                    OccasionModel occasionModel = new OccasionModel(selectedDate, occasionTitle,false, false);
                    occasionModel.setItems(itemsViewModel.getPendingItems().getValue());
                    occasionModel.setLocationModel(location);
                    occasionViewModel.insert(occasionModel);

                    Navigation.findNavController(currentView).navigate(R.id.action_occasionAddFragment_to_tabViewFragment);
                }
                else{
                    if(!Validator.EditTextHasValues(editTexts)){
                        Toast.makeText(getActivity(), "Enter title or date!", Toast.LENGTH_SHORT).show();
                    }
                    if(itemsViewModel.getPendingItems().getValue().size() == 0){
                        Toast.makeText(getActivity(), "Add atleast 1 item!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.buttonCancel:

                Navigation.findNavController(view).navigate(R.id.action_occasionAddFragment_to_tabViewFragment);
                break;
            case R.id.buttonAddItems:
                DialogAddItem dialogFragment = new DialogAddItem();
                dialogFragment.show(getChildFragmentManager(), "Test");
                break;
            case R.id.buttonEnterLocation:
                DialogAddLocation dialogAddLocation = new DialogAddLocation();
                dialogAddLocation.show(getChildFragmentManager(), "Test");
                break;
            case R.id.buttonPickADate:
                DialogDatePicker datePickerDialog = new DialogDatePicker();
                datePickerDialog.show(getChildFragmentManager(), "Test");
                break;
        }
    }
}