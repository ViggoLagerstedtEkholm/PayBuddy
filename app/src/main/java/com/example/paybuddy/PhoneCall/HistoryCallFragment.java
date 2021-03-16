package com.example.paybuddy.PhoneCall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.paybuddy.R;

/**
 *  This fragment shows the phone call history.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class HistoryCallFragment extends Fragment {

    public HistoryCallFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the fragment with the "fragment_history_call.xml".
     * @param inflater inflates the view.
     * @param container view that contains views.
     * @param savedInstanceState latest saved instance.
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonBackCallHistory = view.findViewById(R.id.buttonBackCallHistory);
        Button contacts = view.findViewById(R.id.buttonGoToContacts);

        //Navigate to the tabView Fragment.
        buttonBackCallHistory.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_historyCallFragment_to_tabViewFragment));

        //Navigate to the contact fragment.
        contacts.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_historyCallFragment_to_contactManageFragment));
    }
}