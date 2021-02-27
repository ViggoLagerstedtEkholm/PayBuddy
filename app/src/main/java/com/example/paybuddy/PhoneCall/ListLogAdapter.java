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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.paybuddy.Models.HistoryModel;
import com.example.paybuddy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListLogAdapter extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<HistoryModel> callHistoryModels;
    private RecyclerView recyclerView;
    private HistoryLogAdapter callHistoryAdapter;

    private String time;
    private String date, fullDate;
    private String TelephoneNumber;
    private Button callButton;
    private String TYPE_OF_CALL;

    private SwipeRefreshLayout swipeRefreshLayout;

    //The permissions we need to make the app work
    String[] appPermissions = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };

    private final int PERMISSION_REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_call_history, container, false);

        instantiate(view);

        if(checkForHistoryPermission()){
            populateCallhistory();
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void instantiate(View view)
    {
        swipeRefreshLayout = view.findViewById(R.id.activity_main_swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.activity_main_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        callHistoryModels = new ArrayList<>();
        callHistoryAdapter = new HistoryLogAdapter(getContext(), callHistoryModels);
        recyclerView.setAdapter(callHistoryAdapter);
    }

    @Override
    public void onRefresh() {
        if(checkForHistoryPermission())
        {
            Log.d("REFRESH", "...");
            populateCallhistory();
            swipeRefreshLayout.setRefreshing(false);
        }
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
                if(grantResults[i]== PackageManager.PERMISSION_DENIED){
                    hasFoundDenied = true;
                    break;
                }
            }
            if(!hasFoundDenied){
                populateCallhistory();
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
            if(ContextCompat.checkSelfPermission(getContext(), aPermission) != PackageManager.PERMISSION_GRANTED){
                listOfPermissionsNeeded.add(aPermission);
            }
        }

        if(!listOfPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(getActivity(),listOfPermissionsNeeded.toArray(new String[listOfPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
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
    private void populateCallhistory() {
        String sortingOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Cursor cursor;
        cursor = getContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null, sortingOrder);

        callHistoryModels.clear();

        while (cursor.moveToNext()) {
            fullDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String date = dateFormat.format(new Date(Long.parseLong(fullDate)));

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String time =timeFormat.format(new Date(Long.parseLong(fullDate)));

            TelephoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            TYPE_OF_CALL = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));

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

            HistoryModel historyModel = new HistoryModel(TelephoneNumber, date , time, TYPE_OF_CALL);
            callHistoryModels.add(historyModel);
            Log.d("COUNT: ", String.valueOf(callHistoryModels.size()));
        }
        callHistoryAdapter.notifyDataSetChanged();
    }
}