package com.example.paybuddy.TimesUp.TimesUp.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.paybuddy.Maps.CoordinatesViewModel;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogMakeExpired;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class TimesUpRecyclerViewAdapter extends RecyclerView.Adapter<TimesUpRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> items;
    private List<OccasionModel> filteredItems;
    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private ItemsViewModel itemsViewModel;
    private CoordinatesViewModel coordinatesViewModel;
    private Fragment fragment;

    public TimesUpRecyclerViewAdapter(List<OccasionModel> items,
                                      OccasionViewModel occasionViewModel,
                                      ItemsViewModel itemsViewModel,
                                      LocationViewModel locationViewModel,
                                      Fragment currentFragment)
    {
        this.items = items;
        this.filteredItems = new ArrayList<>();
        this.occasionViewModel = occasionViewModel;
        this.itemsViewModel = itemsViewModel;
        this.locationViewModel = locationViewModel;
        this.fragment = currentFragment;

        coordinatesViewModel = new ViewModelProvider(currentFragment.getActivity()).get(CoordinatesViewModel.class);
    }

    public void addItems(List<OccasionModel> items){
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_due_payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);

        LocationModel location = occasionModel.getLocationModel();
        if(location != null){
            holder.textView_list_due_payment_location_value.setText(location.getAdress());
        }

        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), totalCost -> {
            if(totalCost != null){
                double cost = totalCost.doubleValue();
                holder.textView_list_due_payment_sum_of_items_value.setText(Double.toString(cost));
            }
        });

        itemsViewModel.getOccasionItems(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), items -> {
            if(items != null){
                int count = items.size();
                holder.textView_list_due_payment_names_value.setText(String.valueOf(count));
            }
        });

        holder.buttonLocationDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordinatesViewModel.setLocation(occasionModel.getLocationModel());
                Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_mapsFragment);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewOccasion dialogMakeExpired = new DialogPreviewOccasion(occasionModel);
                dialogMakeExpired.show(fragment.getChildFragmentManager(), "Test");
            }
        });

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsViewModel.delete(occasionModel.getItems());
                locationViewModel.delete(occasionModel.getLocationModel());
                occasionViewModel.delete(occasionModel);
            }
        });

        holder.buttonPostPone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(occasionModel);
                occasionModel.setExpired(false);
                occasionViewModel.update(occasionModel);
            }
        });

        holder.buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_historyCallFragment);
            }
        });

        holder.textView_list_due_payment_title.setText(occasionModel.getDescription());
        holder.textView_list_due_payment_expiringDate_value.setText(occasionModel.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OccasionModel> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0 || constraint.equals("")){
                filteredList.addAll(filteredItems);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(OccasionModel item : filteredItems){
                    if(item.getDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(String.valueOf(((List) results.values).size()), "  Size");
            if(((List) results.values).size() != 0){
                items.clear();
                items.addAll((List)results.values);
                notifyDataSetChanged();
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_list_due_payment_title;
        public final TextView textView_list_due_payment_sum_of_items_value;
        public final TextView textView_list_due_payment_names_value;
        public final TextView textView_list_due_payment_expiringDate_value;
        public final TextView textView_list_due_payment_location_value;

        public final Button buttonCall;
        public final Button buttonPostPone;
        public final Button buttonRemove;
        public final Button buttonLocationDue;

        public OccasionModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            buttonRemove = (Button) view.findViewById(R.id.buttonDeleteDuePayment);
            buttonPostPone = (Button) view.findViewById(R.id.buttonPostpone);
            buttonCall = (Button) view.findViewById(R.id.buttonCall);
            buttonLocationDue = (Button) view.findViewById(R.id.buttonLocationDue);

            textView_list_due_payment_title = (TextView) view.findViewById(R.id.textView_list_due_payment_title);
            textView_list_due_payment_sum_of_items_value = (TextView) view.findViewById(R.id.textView_list_due_payment_sum_of_items_value);
            textView_list_due_payment_names_value = (TextView) view.findViewById(R.id.textView_list_due_payment_names_value);
            textView_list_due_payment_expiringDate_value = (TextView) view.findViewById(R.id.textView_list_due_payment_expiringDate_value);
            textView_list_due_payment_location_value = (TextView) view.findViewById(R.id.textView_list_due_payment_location_value);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView_list_due_payment_title.getText() + "'";
        }
    }
}