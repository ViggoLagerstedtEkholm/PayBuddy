package com.example.paybuddy.Occasions.List_of_occasions;

import androidx.fragment.app.Fragment;
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

import com.example.paybuddy.MVVM.ItemsViewModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
import com.example.paybuddy.MVVM.OccasionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> items;
    private List<OccasionModel> filteredItems;
    private final Fragment currentFragment;
    private final OccasionViewModel occasionViewModel;
    private final ItemsViewModel itemsViewModel;

    public MyItemRecyclerViewAdapter( List<OccasionModel> items, Fragment currentFragment, OccasionViewModel occasionViewModel,
                                      ItemsViewModel itemsViewModel) {
        this.items = items;
        this.currentFragment = currentFragment;
        this.occasionViewModel = occasionViewModel;
        this.itemsViewModel = itemsViewModel;
        this.filteredItems = new ArrayList<>();
    }

    public void addItems(List<OccasionModel> occasionModels){
        this.items = occasionModels;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_occasion_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);
        String people = "";
        double cost = 0.0;

        //for(ItemModel item : occasionModel.getItems()){
         //   cost += item.getPrice() * item.getQuantity();
        //}

        holder.titleOfMyOccasion.setText(occasionModel.getDescription());
        holder.textViewDateOccasionCard.setText(occasionModel.getDate());

        holder.textViewSumOfItemsOccasionCard.setText(Double.toString(cost));
        holder.textViewPeopleOccasionCard.setText("TODO");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(occasionModel);
                dialogFragment.show(currentFragment.getChildFragmentManager(), "Test");
            }
        });

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsViewModel.deleteAllItems();
                occasionViewModel.delete(occasionModel);
                itemsViewModel.delete(occasionModel.getItems());
            }
        });

        holder.buttonRegisterPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                occasionModel.setPaid(true);
                occasionViewModel.update(occasionModel);
            }
        });
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
            items.clear();
            items.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleOfMyOccasion;
        public final TextView textViewDateOccasionCard;
        public final TextView textViewSumOfItemsOccasionCard;
        public final TextView textViewPeopleOccasionCard;
        public final Button buttonRegisterPaid;
        public final Button buttonRemove;
        public OccasionModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            buttonRemove = (Button) view.findViewById(R.id.buttonRemoveItem);
            buttonRegisterPaid = (Button) view.findViewById(R.id.buttonRegisterPaid);

            titleOfMyOccasion = (TextView) view.findViewById(R.id.titleOfMyOccasion);
            textViewDateOccasionCard = (TextView) view.findViewById(R.id.textViewDateOccasionCard);

            textViewSumOfItemsOccasionCard = (TextView) view.findViewById(R.id.textViewSumOfItemsOccasionCard);
            textViewPeopleOccasionCard = (TextView) view.findViewById(R.id.textViewPeopleOccasionCard);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleOfMyOccasion.getText() + "'";
        }
    }
}