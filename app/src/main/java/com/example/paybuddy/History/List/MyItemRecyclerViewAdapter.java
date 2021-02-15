package com.example.paybuddy.History.List;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<OccasionModel> items;

    public MyItemRecyclerViewAdapter(List<OccasionModel> items) {
        this.items = items;
    }

    public void addItems(List<OccasionModel> items){
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);
        String people = "";
        double cost = 0.0;

        for(ItemModel item : occasionModel.getItems()){
            cost += item.getPrice() * item.getQuantity();
        }

        holder.textView_list_history_title.setText(occasionModel.getDescription());
        holder.textView_list_history_expiringDate_value.setText(occasionModel.getDate());
        holder.textView_list_history_sum_of_items_value.setText(Double.toString(cost));
        holder.textView_list_history_names_value.setText("TODO");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_list_history_title;
        public final TextView textView_list_history_sum_of_items_value;
        public final TextView textView_list_history_names_value;
        public final TextView textView_list_history_expiringDate_value;

        public final Button buttonCall;
        public final Button buttonPostPone;
        public final Button buttonRemove;

        public OccasionModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            buttonRemove = (Button) view.findViewById(R.id.buttonDeleteDuePayment);
            buttonPostPone = (Button) view.findViewById(R.id.buttonPostpone);
            buttonCall = (Button) view.findViewById(R.id.buttonCall);

            textView_list_history_title = (TextView) view.findViewById(R.id.textView_list_history_title);
            textView_list_history_sum_of_items_value = (TextView) view.findViewById(R.id.textView_list_history_sum_of_items_value);
            textView_list_history_names_value = (TextView) view.findViewById(R.id.textView_list_history_names_value);
            textView_list_history_expiringDate_value = (TextView) view.findViewById(R.id.textView_list_history_expiringDate_value);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView_list_history_title.getText() + "'";
        }
    }
}