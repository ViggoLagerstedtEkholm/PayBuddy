package com.example.paybuddy.Occasions.List_of_occasions;

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
import com.example.paybuddy.Occasions.Dialogs.DialogMakeExpired;
import com.example.paybuddy.Viewmodels.ItemsViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.LocationViewModel;
import com.example.paybuddy.Viewmodels.OccasionViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a RecyclerView class that displays a list of OccasionModels.
 * We also have observers to receive database items.
 *
 * We use the interface Filterable to filter results in this RecyclerView.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class ActiveOccasionRecyclerViewAdapter extends RecyclerView.Adapter<ActiveOccasionRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> items;
    private List<OccasionModel> filteredItems;
    private final Fragment currentFragment;
    private final OccasionViewModel occasionViewModel;
    private final ItemsViewModel itemsViewModel;
    private final LocationViewModel locationViewModel;
    private final CoordinatesViewModel coordinatesViewModel;

    public ActiveOccasionRecyclerViewAdapter(List<OccasionModel> items,
                                             Fragment currentFragment,
                                             OccasionViewModel occasionViewModel,
                                             ItemsViewModel itemsViewModel,
                                             LocationViewModel locationViewModel) {
        this.items = items;
        this.currentFragment = currentFragment;
        this.occasionViewModel = occasionViewModel;
        this.itemsViewModel = itemsViewModel;
        this.filteredItems = new ArrayList<>();
        this.locationViewModel = locationViewModel;

        coordinatesViewModel = new ViewModelProvider(currentFragment.requireActivity()).get(CoordinatesViewModel.class);
    }

    public void addItems(List<OccasionModel> occasionModels){
        this.items = occasionModels;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    /**
     * This method inflates our "fragment_occasion_item.xml" that is the view for our items in the RecyclerView.
     * @param parent view that contains other views.
     * @param viewType int.
     * @return ViewHolder
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_occasion_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method will be called for every item in the recyclerview. We add listeners and different
     * logic when we click the occasion in the RecyclerView.
     * @param holder class containing widgets.
     * @param position of our item in the items array.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);

        holder.titleOfMyOccasion.setText(occasionModel.getDescription());
        holder.textViewDateOccasionCard.setText(occasionModel.getDate());
        holder.textViewPeopleOccasionCard.setText("0");

        LocationModel location = occasionModel.getLocationModel();
        if(location != null){
            holder.textViewLocationOccasionCard.setText(location.getAddress());
        }

        //Observes the total cost of the
        itemsViewModel.getOccasionTotalCost(occasionModel.getID()).observe(currentFragment.getViewLifecycleOwner(), totalCost -> {
            if(totalCost != null){
                double cost = totalCost.doubleValue();
                holder.textViewSumOfItemsOccasionCard.setText(String.valueOf(cost));
            }
        });

        //Gets the total amount of items in a occasion.
        itemsViewModel.getOccasionItemCount(occasionModel.getID()).observe(currentFragment.getViewLifecycleOwner(), count -> holder.textViewPeopleOccasionCard.setText(String.valueOf(count.intValue())));

        //Sends the location of this occasion to the maps fragment.
        holder.button_see_location.setOnClickListener(v -> {
            coordinatesViewModel.setLocation(occasionModel.getLocationModel());
            Navigation.findNavController(holder.mView).navigate(R.id.action_tabViewFragment_to_mapsFragment);
        });

        //If the user holds a occasion this listener is called.
        holder.itemView.setOnLongClickListener(v -> {
            DialogMakeExpired dialogMakeExpired = new DialogMakeExpired(occasionModel);
            dialogMakeExpired.show(currentFragment.getChildFragmentManager(), "Test");
            return false;
        });

        //Shows a preview dialog.
        holder.itemView.setOnClickListener(v -> {
            DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(occasionModel);
            dialogFragment.show(currentFragment.getChildFragmentManager(), "Test");
        });

        //Remove occasion and items/location to that occasion.
        holder.buttonRemove.setOnClickListener(v -> {
            locationViewModel.delete(occasionModel.getLocationModel());
            occasionViewModel.delete(occasionModel);
            itemsViewModel.delete(occasionModel.getItems());
        });

        //Register occasion as paid.
        holder.buttonRegisterPaid.setOnClickListener(v -> {
            occasionModel.setPaid(true);
            occasionViewModel.update(occasionModel);
        });
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
        public final TextView titleOfMyOccasion;
        public final TextView textViewDateOccasionCard;
        public final TextView textViewSumOfItemsOccasionCard;
        public final TextView textViewPeopleOccasionCard;
        public final TextView textViewLocationOccasionCard;
        public final Button buttonRegisterPaid;
        public final Button buttonRemove;
        public final Button button_see_location;
        public OccasionModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            buttonRemove = view.findViewById(R.id.buttonRemoveItem);
            buttonRegisterPaid = view.findViewById(R.id.buttonRegisterPaid);
            button_see_location = view.findViewById(R.id.button_see_location);

            titleOfMyOccasion = view.findViewById(R.id.titleOfMyOccasion);
            textViewDateOccasionCard = view.findViewById(R.id.textViewDateOccasionCard);

            textViewLocationOccasionCard = view.findViewById(R.id.textViewLocationOccasionCard);

            textViewSumOfItemsOccasionCard = view.findViewById(R.id.textViewSumOfItemsOccasionCard);
            textViewPeopleOccasionCard = view.findViewById(R.id.textViewPeopleOccasionCard);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + titleOfMyOccasion.getText() + "'";
        }
    }
}