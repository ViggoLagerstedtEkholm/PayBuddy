package com.example.paybuddy.Contacts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.paybuddy.Models.Contact;
import com.example.paybuddy.PhoneCall.HistoryLogAdapter;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FilterViewModel filterViewModel;
    private List<Contact> contacts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter adapter;
    private static final int REQUEST_RUNTIME_PERMISSION = 100;

    public ContactsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contacts = new ArrayList<>();
        filterViewModel = new ViewModelProvider(getActivity()).get(FilterViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterViewModel.getSelected().observe(getViewLifecycleOwner(), searchWord ->{
            adapter.getFilter().filter(searchWord);
        });
    }

    @Override
    public void onRefresh() {
        contacts = getContacts();
        adapter.addItems(contacts);
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        instantiate(view);
        contacts = getContacts();
        adapter.addItems(contacts);

        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    private void instantiate(View view)
    {
        swipeRefreshLayout = view.findViewById(R.id.contact_list_swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.contact_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyItemRecyclerViewAdapter(getContext());
        contacts = new ArrayList<>();
        recyclerView.setAdapter(adapter);
    }

    /**
     * This method checks if we have permission, and we query the user for permission before doing anything else.
     * If we get permission we get all the contacts from the user phone.
     * The contact data contains of a name and a phone number. If a given contact doesn't have a number we fill this field with "NO NUMBER".
     * @return List<Contact> All the contacts from the user phone.
     */
    private List<Contact> getContacts() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS); //granted = 0
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_RUNTIME_PERMISSION);
            return new ArrayList<>();
        }
        contacts.clear();

        String phone = "";

        Cursor cur = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // get the phone number
                Cursor pCur = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    phone = pCur.getString(
                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                if(name == null || name.equals("") ){
                    name = "NO NAME";
                }
                if(phone.equals("")){
                    Contact contact = new Contact(name, "NO NUMBER");
                    contacts.add(contact);
                    Log.d("STATUS", "FETCHING DONE: " + String.valueOf(contacts.size()));
                }else{
                    Contact contact = new Contact(name, phone);
                    contacts.add(contact);
                    Log.d("STATUS", "FETCHING DONE: " + String.valueOf(contacts.size()));
                }

                phone = "";
                pCur.close();
            }
        }
        return contacts;
    }

    /**
     * This method does the right logic whenever the user clicks accept. We can therefore fill our adapter with items if the user clicked "Allow this app to use Contacts".
     * @Override We override to add specific behavior.
     * @param grantResults The permission we are asking for.
     * @param permissions The permissions requested.
     * @param requestCode The code we sent when requesting permission.
     * @return void
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    adapter.addItems(getContacts());
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "READ_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}