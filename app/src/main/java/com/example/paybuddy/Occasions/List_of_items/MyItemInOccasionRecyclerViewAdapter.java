package com.example.paybuddy.Occasions.List_of_items;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *  This RecyclerView displays a list items in a particular occasion.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class MyItemInOccasionRecyclerViewAdapter extends RecyclerView.Adapter<MyItemInOccasionRecyclerViewAdapter.ViewHolder> {
    private List<ItemModel> items;
    private final ItemsViewModel itemsViewModel;

    public MyItemInOccasionRecyclerViewAdapter(List<ItemModel> items, ItemsViewModel itemsViewModel) {
        this.items = items;
        this.itemsViewModel = itemsViewModel;
    }

    /**
     * This method adds the parameter list to our items and filtered list.
     * notifyDataSetChanged() notifies the RecyclerView to refresh.
     * @param itemModels List of items we want to add.
     */
    public void addItems(List<ItemModel> itemModels){
        this.items = itemModels;
        notifyDataSetChanged();
    }

    /**
     * This method returns the view from our "fragment_list_due_payment_item.xml" that is the view for our items in the RecyclerView.
     * @param parent parent ViewGroup.
     * @param viewType viewType.
     * @return ViewHolder
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_occasion_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method will be called for every item in the recyclerview.
     * We set the TextViews to the holder values.
     * @param holder class containing widgets.
     * @param position of our item in the items array.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ItemModel itemModel = items.get(position);
        String description = itemModel.getDescription();
        double price = itemModel.getPrice();
        int quantity = itemModel.getQuantity();
        String name = itemModel.getAssignedPerson();

        holder.mItem = itemModel;
        holder.mDescription.setText(description);
        holder.mPrice.setText(String.valueOf(price));
        holder.mQuantity.setText(String.valueOf(quantity));
        holder.mPerson.setText(name);

        holder.deleteItem.setOnClickListener(v -> {
            itemsViewModel.delete(itemModel);
            notifyDataSetChanged();
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
     * This class is a placeholder for every single item in the RecyclerView.
     * @date 2021-03-09
     * @version 1.0
     * @author Viggo Lagerstedt Ekholm
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDescription;
        public final TextView mPrice;
        public final TextView mQuantity;
        public final TextView mPerson;
        public final Button deleteItem;
        public ItemModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            deleteItem = view.findViewById(R.id.buttonRemoveItem);
            mDescription = view.findViewById(R.id.titleOfMyOccasion);
            mPrice = view.findViewById(R.id.textViewPrice);
            mQuantity = view.findViewById(R.id.textViewQuantity);
            mPerson = view.findViewById(R.id.textViewPersonName);
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mDescription.getText() + "'";
        }
    }
}