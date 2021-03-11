package com.example.paybuddy.Occasions;

import android.os.Bundle;
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
import java.util.List;

/**
 *  This fragment shows the "Add occasion" page. Here the user can add date/items/locations.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class OccasionAddFragment extends Fragment{
    private LocationModel location;
    private EditText title;
    private View currentView;

    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private DateViewModel dateViewModel;
    private ItemsViewModel itemsViewModel;

    private final List<EditText> editTexts = new ArrayList<>();
    private String selectedDate;
    private TextView textDateDisplay;
    private boolean hasItems;

    public OccasionAddFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState last saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        occasionViewModel = new ViewModelProvider(this).get(OccasionViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
    }

    /**
     * This method inflates our "fragment_item.xml" view.
     * @param inflater inflater for our view.
     * @param container view that contains other views.
     * @param savedInstanceState latest saved instance.
     * @return the inflated view is returned.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    /**
     * This method gets all the Buttons/EditTexts.
     * It also observes the ViewModel for pending items.
     * @param view the inflated view.
     * @param savedInstanceState saved bundle.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.currentView = view;

        title = view.findViewById(R.id.FieldTitle);
        textDateDisplay = view.findViewById(R.id.textDateDisplay);

        editTexts.add(title);

        TextView locationAdded = view.findViewById(R.id.textAddedLocation);
        TextView textTotalItems = view.findViewById(R.id.textTotalItemsLabel);

        //Get the buttons.
        Button buttonSave = view.findViewById(R.id.buttonEnter);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonAddItems = view.findViewById(R.id.buttonAddItems);
        Button buttonEnterLocation = view.findViewById(R.id.buttonEnterLocation);
        Button buttonChooseDate = view.findViewById(R.id.buttonPickADate);
        Button buttonClearPendingItems = view.findViewById(R.id.buttonClearPendingItems);

        //Add listeners.
        buttonSave.setOnClickListener(v -> {
            //Check if all fields have values.
            if(Validator.EditTextHasValues(editTexts) && location != null && !selectedDate.equals("") && hasItems)
            {
                String occasionTitle = title.getText().toString();

                //Create new occasion and assign the location/items/date etc.
                OccasionModel occasionModel = new OccasionModel(selectedDate, occasionTitle,false, false);
                occasionModel.setItems(itemsViewModel.getPendingItems().getValue());
                occasionModel.setLocationModel(location);
                occasionViewModel.insert(occasionModel);

                //Navigate back to the main TabView.
                Navigation.findNavController(currentView).navigate(R.id.action_occasionAddFragment_to_tabViewFragment);
            }
            else{
                Toast.makeText(getContext(), "Make sure to add items/date/location and a title.", Toast.LENGTH_SHORT).show();
            }
        });

        //Open a dialog for adding items.
        buttonCancel.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_occasionAddFragment_to_tabViewFragment));

        //Open a dialog for adding items.
        buttonAddItems.setOnClickListener(v -> {
            DialogAddItem dialogFragment = new DialogAddItem();
            dialogFragment.show(getChildFragmentManager(), "Test");
        });

        //Open a dialog for adding location.
        buttonEnterLocation.setOnClickListener(v -> {
            DialogAddLocation dialogAddLocation = new DialogAddLocation();
            dialogAddLocation.show(getChildFragmentManager(), "Test");
        });

        //Opens a date picker dialog.
        buttonChooseDate.setOnClickListener(v -> {
            DialogDatePicker datePickerDialog = new DialogDatePicker();
            datePickerDialog.show(getChildFragmentManager(), "Test");
        });

        buttonClearPendingItems.setOnClickListener(v -> itemsViewModel.deletePendingItems());

        //Get the location.
        locationViewModel.getLocation().observe(getViewLifecycleOwner(), locationModel -> {
            this.location = locationModel;
            locationAdded.setText(location.getAddress());
        });

        //Get the date.
        dateViewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            this.selectedDate = date;
            textDateDisplay.setText(date);
        });

        //Get all the pending items.
        itemsViewModel.getPendingItems().observe(getViewLifecycleOwner(), itemModels -> {
            int size = itemModels.size();
            textTotalItems.setText(String.valueOf(size));

            hasItems = size > 0;
        });
    }
}