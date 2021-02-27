package com.example.paybuddy.Contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.paybuddy.Models.Contact;
import com.example.paybuddy.R;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contacts;

    public MyItemRecyclerViewAdapter(Context context) { this.context = context; }

    /**
     * Constructor that takes an array of models that should be mapped to our adapter.
     */
    public void addItems(List<Contact> items) {
        contacts = items;
    }

    /**
     * Called when we create the ViewHolder.
     * We return a ViewHolder that has our fragment_contacts XML inflated.
     * @Override We override to add specific behavior.
     * @param parent The parent ViewGroup.
     * @param viewType The most recent saved instance.
     * @return ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contacts, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called when we want to bind items to the view holder.
     * @Override We override to add specific behavior.
     * @param holder the holder that will hold item data.
     * @param position the position of specific item.
     * @return void
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact aContact = contacts.get(position);
        String Name = aContact.getName();
        String phoneNr = aContact.getPhoneNumber();

        holder.mItem = aContact;
        holder.mIdView.setText(Name);
        holder.mContentView.setText(phoneNr);

        holder.buttonCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + aContact.getPhoneNumber()));
                context.startActivity(intent);
            }
        });
    }

    /**
     * Returns the list size.
     * @Override We override to add specific behavior.
     * @return int
     */
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    /**
     * This class represent all data that each item should contain.
     * We use this class to contain all of our data of contacts.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Contact mItem;
        public final Button buttonCallContact;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            buttonCallContact = (Button) view.findViewById(R.id.buttonCallContact);
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}