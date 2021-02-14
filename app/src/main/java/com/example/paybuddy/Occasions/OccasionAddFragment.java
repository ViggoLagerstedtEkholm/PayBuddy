package com.example.paybuddy.Occasions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogAddItem;
import com.example.paybuddy.Occasions.Dialogs.DialogOccasionAdded;
import com.example.paybuddy.Occasions.ViewModel.CompleteListViewModel;
import com.example.paybuddy.Occasions.ViewModel.InputToItemListViewModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Repositories.RepositoryViewModel;
import com.example.paybuddy.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OccasionAddFragment extends Fragment implements View.OnClickListener {
    private View currentView;
    private List<ItemModel> items;
    private EditText title;
    private EditText date;
    private InputToItemListViewModel inputToItemListViewModel;
    private CompleteListViewModel completeListViewModel;
    private RepositoryViewModel repositoryViewModel;

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
        repositoryViewModel = new ViewModelProvider(requireActivity()).get(RepositoryViewModel.class);
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

        inputToItemListViewModel.getItem().observe(getViewLifecycleOwner(), itemModel -> {
            items.add(itemModel);
            Log.d("Item size", "... - " + items.size());
        });

        completeListViewModel.getItem().observe(getViewLifecycleOwner(), itemModel -> {
            items = itemModel;
            Log.d("Item size", "... - " + items.size());
        });

        Button buttonSave = (Button) view.findViewById(R.id.buttonEnter);
        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        Button buttonAddItems = (Button) view.findViewById(R.id.buttonAddItems);

        title = (EditText) view.findViewById(R.id.FieldTitle);
        date = (EditText) view.findViewById(R.id.FieldDate);

        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonAddItems.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonEnter:
                //fetch fields etc...
                String occasionTitle = title.getText().toString();
                String occasionDate = date.getText().toString();
                if(!occasionTitle.equals("") && !occasionDate.equals("") && items.size() != 0){
                    OccasionModel occasionModel = new OccasionModel(-1, occasionDate, occasionTitle, items, false, false);

                    repositoryViewModel.insertOccasion(occasionModel);

                    DialogOccasionAdded dialogFragment = new DialogOccasionAdded(occasionModel, currentView);
                    dialogFragment.show(getChildFragmentManager(), "Test");
                }
                else{
                    if(occasionTitle.equals("") && occasionDate.equals("")){
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
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //getActivity().getViewModelStore().
    }
}