package com.example.paybuddy.Contacts;

import android.Manifest;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.paybuddy.Models.Contact;
import com.example.paybuddy.R;
import com.example.paybuddy.Search.SearchViewModels.FilterContactViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  This Fragment displays the contacts of the user.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FilterContactViewModel filterContactViewModel;
    private List<Contact> contacts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContactsRecyclerViewAdapter adapter;
    private static final int REQUEST_RUNTIME_PERMISSION = 100;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiate all the ViewModels.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contacts = new ArrayList<>();
        filterContactViewModel = new ViewModelProvider(requireActivity()).get(FilterContactViewModel.class);
    }

    /**
     * This method is called when the fragment is created.
     * @param view the fragment view.
     * @param savedInstanceState latest saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterContactViewModel.getSelected().observe(getViewLifecycleOwner(), searchWord -> adapter.getFilter().filter(searchWord));
    }

    /**
     * This method gets called when we refresh the list by swiping upwards in the list.
     */
    @Override
    public void onRefresh() {
        contacts = getContacts();
        adapter.addItems(contacts);
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * This method will create get the contacts from the phone and add them to the adapter.
     * @param inflater inflater for our view.
     * @param container view that contains other views.
     * @param savedInstanceState latest saved instance.
     * @return View
     */
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

    /**
     * Setup the RecyclerView.
     * @param view the fragment view.
     */
    private void instantiate(View view)
    {
        swipeRefreshLayout = view.findViewById(R.id.contact_list_swipe_refresh_layout);
        RecyclerView recyclerView = view.findViewById(R.id.contact_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactsRecyclerViewAdapter(getContext());
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
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_CONTACTS); //granted = 0
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_RUNTIME_PERMISSION);
            return new ArrayList<>();
        }
        contacts.clear();

        String phone = "";

        Cursor cursor = requireContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // get the phone number
                Cursor pCur = requireContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
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
                    Log.d("STATUS", "FETCHING DONE: " + contacts.size());
                }else{
                    Contact contact = new Contact(name, phone);
                    contacts.add(contact);
                    Log.d("STATUS", "FETCHING DONE: " + contacts.size());
                }

                phone = "";
                pCur.close();
            }
        }
        cursor.close();
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
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_RUNTIME_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                adapter.addItems(getContacts());
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "READ_CONTACTS Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}