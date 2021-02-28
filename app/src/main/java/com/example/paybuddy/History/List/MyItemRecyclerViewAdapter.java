package com.example.paybuddy.History.List;

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

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.R;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> filteredItems;
    private List<OccasionModel> items;
    private Context context;

    public MyItemRecyclerViewAdapter(Context context, List<OccasionModel> items) {
        this.filteredItems = new ArrayList<>();
        this.context = context;
        this.items = items;
    }

    public void addItems(List<OccasionModel> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_list_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("count", String.valueOf(items.size()));
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
        return items == null ? 0 : items.size();
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