package com.example.paybuddy.Occasions.List_of_items;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Occasions.ViewModel.CompleteListViewModel;
import com.example.paybuddy.R;

import java.util.List;

public class MyItemInOccasionRecyclerViewAdapter extends RecyclerView.Adapter<MyItemInOccasionRecyclerViewAdapter.ViewHolder> {
    private final List<ItemModel> items;
    private CompleteListViewModel completeListViewModel;

    public MyItemInOccasionRecyclerViewAdapter(List<ItemModel> items, CompleteListViewModel completeListViewModel) {
        this.items = items;
        this.completeListViewModel = completeListViewModel;
    }

    public void clearList(){
        items.clear();
        notifyDataSetChanged();
    }

    public void addItemToList(ItemModel item){
        items.add(item);
        Log.d("ADAPTER SIZE: ", String.valueOf(items.size()));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_occasion_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ItemModel itemModel = items.get(position);
        String description = itemModel.getDescription();
        double price = itemModel.getPrice();
        int quantity = itemModel.getQuantity();

        holder.mItem = itemModel;
        holder.mDescription.setText(description);
        holder.mPrice.setText(String.valueOf(price));
        holder.mQuantity.setText(String.valueOf(quantity));
        holder.mPerson.setText("temp");

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                completeListViewModel.setItem(items);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            deleteItem = (Button) view.findViewById(R.id.buttonRemoveItem);
            mDescription = (TextView) view.findViewById(R.id.titleOfMyOccasion);
            mPrice = (TextView) view.findViewById(R.id.textViewPrice);
            mQuantity = (TextView) view.findViewById(R.id.textViewQuantity);
            mPerson = (TextView) view.findViewById(R.id.textViewPersonName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescription.getText() + "'";
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        items.clear();
        notifyDataSetChanged();
    }
}