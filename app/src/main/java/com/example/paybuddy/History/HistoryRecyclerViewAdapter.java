package com.example.paybuddy.History;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 *  This RecyclerView displays a list history occasions.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> filteredItems;
    private List<OccasionModel> items;
    private final Fragment fragment;

    private final LocationViewModel locationViewModel;
    private final OccasionViewModel occasionViewModel;
    private final ItemsViewModel itemsViewModel;
    private final CoordinatesViewModel coordinatesViewModel;

    //Constructor that takes required parameter values.
    public HistoryRecyclerViewAdapter(List<OccasionModel> items, Fragment fragment) {
        this.filteredItems = new ArrayList<>();
        this.items = items;
        this.fragment = fragment;

        this.occasionViewModel = new ViewModelProvider(fragment.requireActivity()).get(OccasionViewModel.class);
        this.itemsViewModel = new ViewModelProvider(fragment.requireActivity()).get(ItemsViewModel.class);
        this.locationViewModel = new ViewModelProvider(fragment.requireActivity()).get(LocationViewModel.class);
        this.coordinatesViewModel = new ViewModelProvider(fragment.requireActivity()).get(CoordinatesViewModel.class);
    }

    /**
     * This method adds the parameter list to our items and filtered list.
     * notifyDataSetChanged() notifies the RecyclerView to refresh.
     * @param items List of items we want to add.
     */
    public void addItems(List<OccasionModel> items){
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
        this.notifyDataSetChanged();
    }

    /**
     * This method returns the view from our "fragment_list_due_payment_item.xml" that is the view for our items in the RecyclerView.
     * @param parent parent ViewGroup.
     * @param viewType viewType.
     * @return ViewHolder
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.fragment_list_history_item, parent, false);
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
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);
        LocationModel location = occasionModel.getLocationModel();

        if(location != null){
            holder.textView_list_history_location_value.setText(location.getAddress());
        }

        //Click listener, sets the ViewModel location to the occasion location.
        holder.buttonHistoryLocation.setOnClickListener(v -> {
            coordinatesViewModel.setLocation(occasionModel.getLocationModel());
            Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_mapsFragment);
        });

        //Click listener, sets a history occasion to paid.
        holder.buttonPostPone.setOnClickListener(v -> {
            occasionModel.setPaid(false);
            occasionViewModel.update(occasionModel);
        });

        //Click listener, removes history occasion.
        holder.buttonRemoveHistory.setOnClickListener(v -> {
            locationViewModel.delete(occasionModel.getLocationModel());
            occasionViewModel.delete(occasionModel);
            itemsViewModel.delete(occasionModel.getItems());
        });

        //Observe occasion total cost by occasion ID.
        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), totalCost -> {
            if(totalCost != null){
                double cost = totalCost.doubleValue();
                holder.textView_list_history_sum_of_items_value.setText(String.valueOf(cost));
            }
        });

        //Click listener, opens DialogPreviewOccasion.
        holder.itemView.setOnClickListener(v -> {
            DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(occasionModel);
            dialogFragment.show(fragment.getChildFragmentManager(), "Test");
        });

        //Observe occasion items for occasion by ID.
        itemsViewModel.getOccasionItems(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), items -> {
            if(items != null){
                int count = items.size();
                holder.textView_list_history_names_value.setText(String.valueOf(count));
            }
        });

        holder.textView_list_history_title.setText(occasionModel.getDescription());
        holder.textView_list_history_expiringDate_value.setText(occasionModel.getDate());
    }

    /**
     * This method returns the items size.
     * @return int size.
     */
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
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
            List<OccasionModel> filteredList = new ArrayList<>();

            //Check if empty.
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(filteredItems);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                //Try to find a match.
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

        /**
         * This method fills our items list with the filtered item list.
         * @param constraint sequence of char.
         * @param results filtered results.
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(((List) results.values).size() != 0) {
                items.clear();
                items.addAll((List) results.values);
                notifyDataSetChanged();
            }
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