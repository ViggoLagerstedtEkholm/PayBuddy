package com.example.paybuddy.History.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.paybuddy.Maps.CoordinatesViewModel;
import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> filteredItems;
    private List<OccasionModel> items;
    private final Fragment fragment;

    private final LocationViewModel locationViewModel;
    private final OccasionViewModel occasionViewModel;
    private final ItemsViewModel itemsViewModel;
    private final CoordinatesViewModel coordinatesViewModel;

    public HistoryRecyclerViewAdapter(List<OccasionModel> items,
                                      Fragment fragment,
                                      OccasionViewModel occasionViewModel,
                                      LocationViewModel locationViewModel,
                                      ItemsViewModel itemsViewModel) {
        this.filteredItems = new ArrayList<>();
        this.items = items;
        this.fragment = fragment;

        this.occasionViewModel = occasionViewModel;
        this.itemsViewModel = itemsViewModel;
        this.locationViewModel = locationViewModel;

        coordinatesViewModel = new ViewModelProvider(fragment.requireActivity()).get(CoordinatesViewModel.class);
    }

    public void addItems(List<OccasionModel> items){
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.fragment_list_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);
        LocationModel location = occasionModel.getLocationModel();

        if(location != null){
            holder.textView_list_history_location_value.setText(location.getAddress());
        }

        holder.buttonHistoryLocation.setOnClickListener(v -> {
            coordinatesViewModel.setLocation(occasionModel.getLocationModel());
            Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_mapsFragment);
        });

        holder.buttonPostPone.setOnClickListener(v -> {
            Log.d("Updating?", "??");
            occasionModel.setPaid(false);
            occasionViewModel.update(occasionModel);
        });

        holder.buttonRemoveHistory.setOnClickListener(v -> {
            locationViewModel.delete(occasionModel.getLocationModel());
            occasionViewModel.delete(occasionModel);
            itemsViewModel.delete(occasionModel.getItems());
        });

        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), totalCost -> {
            if(totalCost != null){
                double cost = totalCost.doubleValue();
                holder.textView_list_history_sum_of_items_value.setText(Double.toString(cost));
            }
        });

        holder.itemView.setOnClickListener(v -> {
            DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(occasionModel);
            dialogFragment.show(fragment.getChildFragmentManager(), "Test");
        });

        itemsViewModel.getOccasionItems(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), items -> {
            if(items != null){
                int count = items.size();
                holder.textView_list_history_names_value.setText(String.valueOf(count));
            }
        });

        holder.textView_list_history_title.setText(occasionModel.getDescription());
        holder.textView_list_history_expiringDate_value.setText(occasionModel.getDate());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OccasionModel> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
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
            if(((List) results.values).size() != 0) {
                items.clear();
                items.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_list_history_title;
        public final TextView textView_list_history_sum_of_items_value;
        public final TextView textView_list_history_names_value;
        public final TextView textView_list_history_expiringDate_value;
        public final TextView textView_list_history_location_value;

        public final Button buttonCall;
        public final Button buttonPostPone;
        public final Button buttonRemove;
        public final Button buttonRemoveHistory;
        public final Button buttonHistoryLocation;

        public OccasionModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            buttonRemove =view.findViewById(R.id.buttonDeleteDuePayment);
            buttonPostPone = view.findViewById(R.id.buttonHistoryPostPone);
            buttonCall = view.findViewById(R.id.buttonCall);
            buttonRemoveHistory = view.findViewById(R.id.buttonRemoveHistory);
            buttonHistoryLocation = view.findViewById(R.id.buttonHistoryLocation);

            textView_list_history_title = view.findViewById(R.id.textView_list_history_title);
            textView_list_history_sum_of_items_value = view.findViewById(R.id.textView_list_history_sum_of_items_value);
            textView_list_history_names_value = view.findViewById(R.id.textView_list_history_names_value);
            textView_list_history_expiringDate_value = view.findViewById(R.id.textView_list_history_expiringDate_value);
            textView_list_history_location_value = view.findViewById(R.id.textView_list_history_location_value);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + textView_list_history_title.getText() + "'";
        }
    }
}