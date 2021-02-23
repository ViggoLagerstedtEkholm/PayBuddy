package com.example.paybuddy.Occasions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogAddItem;
import com.example.paybuddy.Occasions.Dialogs.DialogAddLocation;
import com.example.paybuddy.Occasions.Dialogs.DialogDatePicker;
import com.example.paybuddy.Occasions.Dialogs.DialogOccasionAdded;
import com.example.paybuddy.Occasions.ViewModel.CompleteListViewModel;
import com.example.paybuddy.Occasions.ViewModel.DateViewModel;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.Occasions.ViewModel.LocationViewModel;
import com.example.paybuddy.R;
import com.example.paybuddy.MVVM.OccasionViewModel;
import com.example.paybuddy.Validator;

import java.util.ArrayList;
import java.util.List;

public class OccasionAddFragment extends Fragment implements View.OnClickListener {
    private View currentView;
    private List<ItemModel> items;
    private LocationModel location;
    private EditText title;
    private InputToItemListViewModel inputToItemListViewModel;
    private CompleteListViewModel completeListViewModel;
    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private DateViewModel dateViewModel;
    private List<EditText> editTexts = new ArrayList<>();
    public static final int REQUEST_CODE = 100;
    private String selectedDate;
    private TextView textDateDisplay;

    public OccasionAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        items = new ArrayList<>();
        inputToItemListViewModel = new ViewModelProvider(this).get(InputToItemListViewModel.class);
        completeListViewModel = new ViewModelProvider(this).get(CompleteListViewModel.class);
        occasionViewModel = new ViewModelProvider(requireActivity()).get(OccasionViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
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
        currentView = view;

        TextView locationAdded = (TextView) view.findViewById(R.id.textAddedLocation);
        TextView textTotalItems = (TextView) view.findViewById(R.id.textTotalItems);
        textDateDisplay = (TextView) view.findViewById(R.id.textDateDisplay);

        dateViewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            selectedDate = date;
            textDateDisplay.setText(date);
        });

        locationViewModel.getLocation().observe(getViewLifecycleOwner(), locationModel -> {
            location = locationModel;
            if(location != null){
                locationAdded.setText("Location added!");
            }
        });

        inputToItemListViewModel.getItem().observe(getViewLifecycleOwner(), itemModel -> {
            items.add(itemModel);
            Log.d("Item size", "... - " + items.size());
            if(items.size() > 0){
                textTotalItems.setText("Total items: " + items.size());
            }
        });

        completeListViewModel.getItem().observe(getViewLifecycleOwner(), itemModel -> {
            items = itemModel;
            Log.d("Item size", "... - " + items.size());
            if(items.size() > 0){
                textTotalItems.setText("Total items: " + items.size());
            }
        });

        Button buttonSave = (Button) view.findViewById(R.id.buttonEnter);
        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        Button buttonAddItems = (Button) view.findViewById(R.id.buttonAddItems);
        Button buttonEnterLocation = (Button) view.findViewById(R.id.buttonEnterLocation);
        Button buttonChooseDate = (Button) view.findViewById(R.id.buttonPickADate);
        buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDatePicker datePickerDialog = new DialogDatePicker();
                datePickerDialog.show(getChildFragmentManager(), "Test");
            }
        });

        title = (EditText) view.findViewById(R.id.FieldTitle);

        editTexts.add(title);

        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonAddItems.setOnClickListener(this);
        buttonEnterLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonEnter:
                if(Validator.EditTextHasValues(editTexts) && location != null && selectedDate != "")
                {
                    String occasionTitle = title.getText().toString();
                    OccasionModel occasionModel = new OccasionModel(selectedDate, occasionTitle,false, false);
                    occasionModel.setItems(items);
                    occasionModel.setLocationModel(location);
                    occasionViewModel.insert(occasionModel);

                    DialogOccasionAdded dialogFragment = new DialogOccasionAdded(occasionModel, currentView);
                    dialogFragment.show(getChildFragmentManager(), "Test");
                }
                else{
                    if(!Validator.EditTextHasValues(editTexts)){
                        Toast.makeText(getActivity(), "Enter title or date!", Toast.LENGTH_SHORT).show();
                    }
                    if(items.size() == 0){
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
        }
    }
}