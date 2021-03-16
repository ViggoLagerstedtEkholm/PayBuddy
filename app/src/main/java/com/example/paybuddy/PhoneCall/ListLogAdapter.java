package com.example.paybuddy.PhoneCall;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.paybuddy.Models.HistoryModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterPhoneHistoryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ListLogAdapter extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<HistoryModel> callHistoryModels;
    private HistoryLogRecyclerViewAdapter callHistoryAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FilterPhoneHistoryViewModel filterPhoneHistoryViewModel;

    //The permissions we need to make the app work
    String[] appPermissions = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };

    private final int PERMISSION_REQUEST_CODE = 101;

    /**
     * Create the ViewModel.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filterPhoneHistoryViewModel = new ViewModelProvider(requireActivity()).get(FilterPhoneHistoryViewModel.class);
    }

    /**
     * Check if we have permission, if that is the case we instantly fill the list with items. Otherwise
     * we query the user for permission.
     * @param inflater inflates the view.
     * @param container view that contains other views.
     * @param savedInstanceState latest saved instance.
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_call_history, container, false);

        instantiate(view);

        if(checkForHistoryPermission())
        {
            populateCallHistory();
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    /**
     * Setup the RecyclerView.
     * @param view fragment view.
     */
    private void instantiate(View view)
    {
        swipeRefreshLayout = view.findViewById(R.id.activity_main_swipe_refresh_layout);
        RecyclerView recyclerView = view.findViewById(R.id.activity_main_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        callHistoryAdapter = new HistoryLogRecyclerViewAdapter(getContext());
        callHistoryModels = new ArrayList<>();
        recyclerView.setAdapter(callHistoryAdapter);
    }

    /**
     * When we swipe to refresh this method gets called and checks if we have permission and updates the
     * RecyclerView.
     */
    @Override
    public void onRefresh() {
        if(checkForHistoryPermission())
        {
            Log.d("REFRESH", "...");
            populateCallHistory();
            swipeRefreshLayout.setRefreshing(false);
        }
        else{
            Toast.makeText(getActivity(), "Permission was not granted!", Toast.LENGTH_SHORT).show();
        }

        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Observe the filter ViewModel to see if we have a filter.
     * @param view the fragment view.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterPhoneHistoryViewModel.getSelected().observe(getViewLifecycleOwner(), searchWord -> callHistoryAdapter.getFilter().filter(searchWord));
    }

    /**
     * We go through all of the permissions requested, if any of them is denied we will not populate the screen with call history.
     * @Override we specify specific behavior.
     * @param requestCode the code we passed when we sent the request.
     * @param permissions the permissions we wanted.
     * @param grantResults The result of our request to get the permission.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasFoundDenied = false;
        if(requestCode == PERMISSION_REQUEST_CODE){
            for(int i = 0; i < permissions.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    hasFoundDenied = true;
                    break;
                }
            }
            if(!hasFoundDenied){
                populateCallHistory();
            }
        }
    }

    /**
     * This method checks what permissions we need, if one is not granted we will add this to our list of needed permissions.
     * If the list is not empty we will ask for all permissions in the array.
     * @return boolean
     */
    private boolean checkForHistoryPermission(){
        ArrayList<String> listOfPermissionsNeeded = new ArrayList<>();
        for(String aPermission : appPermissions){
            if(checkSelfPermission(requireContext(), aPermission) != PackageManager.PERMISSION_GRANTED){
                listOfPermissionsNeeded.add(aPermission);
            }
        }

        if(!listOfPermissionsNeeded.isEmpty()){
            requestPermissions(listOfPermissionsNeeded.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    /**
     * This method will fetch all the calls in our local database. We will add all of these to our adapter and refresh it once it's done.
     * We should also see what type the call is, what time/date, phone number.
     * After each iteration we create a new HistoryModel object that holds the fetched columns.
     * @return void
     */
    private void populateCallHistory() {
        String sortingOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Cursor cursor =
                requireContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, sortingOrder);

        callHistoryModels.clear();

        while (cursor.moveToNext()) {
            String fullDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            String date = dateFormat.format(new Date(Long.parseLong(fullDate)));

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            String time =timeFormat.format(new Date(Long.parseLong(fullDate)));

            String telephoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String TYPE_OF_CALL = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));

            switch (Integer.parseInt(TYPE_OF_CALL)) {
                case CallLog.Calls.INCOMING_TYPE:
                    TYPE_OF_CALL = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    TYPE_OF_CALL = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    TYPE_OF_CALL = "Missed";
                    break;
                case CallLog.Calls.VOICEMAIL_TYPE:
                    TYPE_OF_CALL = "Voicemail";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    TYPE_OF_CALL = "Rejected";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    TYPE_OF_CALL = "Blocked";
                    break;
                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                    TYPE_OF_CALL = "Externally Answered";
                    break;
                default:
                    TYPE_OF_CALL = "NA";
            }

            HistoryModel historyModel = new HistoryModel(telephoneNumber, date , time, TYPE_OF_CALL);
            callHistoryModels.add(historyModel);
        }
        cursor.close();
        callHistoryAdapter.addItems(callHistoryModels);
    }
}