package com.example.paybuddy.Contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.paybuddy.Models.Contact;
import com.example.paybuddy.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  This RecyclerView displays the contacts of the user.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private List<Contact> contacts;
    private List<Contact> filteredItems;

    //Constructor that takes the context.
    public ContactsRecyclerViewAdapter(Context context) {
        this.context = context;
        this.filteredItems = new ArrayList<>();
    }

    /**
     * This method adds the parameter list to our items and filtered list.
     * notifyDataSetChanged() notifies the RecyclerView to refresh.
     * @param items List of items we want to add.
     */
    public void addItems(List<Contact> items) {
        this.contacts = items;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    /**
     * This method returns the view from our "fragment_contacts.xml" that is the view for our items in the RecyclerView.
     * @param parent parent ViewGroup.
     * @param viewType viewType.
     * @return ViewHolder
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contacts, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method will be called for every item in the recyclerview.
     * logic when we click the occasion in the RecyclerView.
     * @param holder class containing widgets.
     * @param position of our item in the items array.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact aContact = contacts.get(position);
        String Name = aContact.getName();
        String phoneNr = aContact.getPhoneNumber();

        holder.mItem = aContact;
        holder.mIdView.setText(Name);
        holder.mContentView.setText(phoneNr);

        holder.buttonCallContact.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + aContact.getPhoneNumber()));
            context.startActivity(intent);
        });
    }

    /**
     * This method returns the items size.
     * @return int size.
     */
    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    /**
     * This method returns the filter for this RecyclerView.
     * @return Filter
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * Creates a new filter.
     * @return Filter
     */
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(filteredItems);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Contact item : filteredItems){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        /**
         * This method fills our items list with the filtered item list.
         * @param constraint sequence of char.
         * @param results filtered results.
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    /**
     * This class is a placeholder for every single item in the RecyclerView.
     * @date 2021-03-09
     * @version 1.0
     * @author Viggo Lagerstedt Ekholm
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Contact mItem;
        public final Button buttonCallContact;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            buttonCallContact = view.findViewById(R.id.buttonCallContact);
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}