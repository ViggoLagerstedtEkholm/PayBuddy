package com.example.paybuddy.TimesUp.TimesUp.List;

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

import java.util.ArrayList;
import java.util.List;

/**
 * This is a RecyclerView class that displays a list of OccasionModel.
 * We also have a bunch of observers to receive database items.
 *
 * We use the interface Filterable to filter results in this RecyclerView.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class TimesUpRecyclerViewAdapter extends RecyclerView.Adapter<TimesUpRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> items;
    private List<OccasionModel> filteredItems;
    private OccasionViewModel occasionViewModel;
    private LocationViewModel locationViewModel;
    private ItemsViewModel itemsViewModel;
    private CoordinatesViewModel coordinatesViewModel;
    private Fragment fragment;

    //Constructor that takes required parameter values.
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

    /**
     * This method adds the parameter list to our items and filtered list.
     * @param items List of items we want to add.
     */
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

        //Observe the total cost for a occasion.
        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), totalCost -> {
            if(totalCost != null){
                double cost = totalCost.doubleValue();
                holder.textView_list_due_payment_sum_of_items_value.setText(Double.toString(cost));
            }
        });

        //Observe the occasion items for a occasion id.
        itemsViewModel.getOccasionItems(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), items -> {
            if(items != null){
                int count = items.size();
                holder.textView_list_due_payment_names_value.setText(String.valueOf(count));
            }
        });

        //If we click the location button.
        holder.buttonLocationDue.setOnClickListener(v -> {
            coordinatesViewModel.setLocation(occasionModel.getLocationModel());
            Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_mapsFragment);
        });

        //If we click the expired occasion item.
        holder.mView.setOnClickListener(v -> {
            DialogPreviewOccasion dialogMakeExpired = new DialogPreviewOccasion(occasionModel);
            dialogMakeExpired.show(fragment.getChildFragmentManager(), "Test");
        });

        //Remove a expired occasion.
        holder.buttonRemove.setOnClickListener(v -> {
            itemsViewModel.delete(occasionModel.getItems());
            locationViewModel.delete(occasionModel.getLocationModel());
            occasionViewModel.delete(occasionModel);
        });

        //Postpone a expired occasion.
        holder.buttonPostPone.setOnClickListener(v -> {
            items.remove(occasionModel);
            occasionModel.setExpired(false);
            occasionViewModel.update(occasionModel);
        });

        holder.buttonCall.setOnClickListener(v -> Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_historyCallFragment));

        holder.textView_list_due_payment_title.setText(occasionModel.getDescription());
        holder.textView_list_due_payment_expiringDate_value.setText(occasionModel.getDate());
    }

    /**
     * This method returns the items size.
     * @return int size.
     */
    @Override
    public int getItemCount() {
        return items.size();
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
     * This gets called if we change the query text in the SeachView for the Search fragment.
     * @return Filter
     */
    private Filter filter = new Filter() {
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
         * @param constraint
         * @param results filtered results.
         */
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

    /**
     * This class is a placeholder for every single item in the RecyclerView.
     * @date 2021-03-09
     * @version 1.0
     * @author Viggo Lagerstedt Ekholm
     */
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

        //Fetch all fields.
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