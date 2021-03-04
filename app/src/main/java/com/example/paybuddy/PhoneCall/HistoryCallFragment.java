package com.example.paybuddy.PhoneCall;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.paybuddy.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HistoryCallFragment extends Fragment {

    public HistoryCallFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonBackCallHistory = view.findViewById(R.id.buttonBackCallHistory);
        Button contacts = view.findViewById(R.id.buttonGoToContacts);

        buttonBackCallHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_historyCallFragment_to_tabViewFragment);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_historyCallFragment_to_contactManageFragment);
            }
        });
    }
}