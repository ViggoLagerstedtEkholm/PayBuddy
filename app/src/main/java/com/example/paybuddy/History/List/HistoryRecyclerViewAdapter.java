package com.example.paybuddy.History.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;
import com.example.paybuddy.Viewmodels.ItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<OccasionModel> filteredItems;
    private List<OccasionModel> items;
    private Context context;
    private Fragment fragment;
    private ItemsViewModel itemViewModel;

    public HistoryRecyclerViewAdapter(Context context, List<OccasionModel> items, Fragment fragment, ItemsViewModel itemViewModel) {
        this.filteredItems = new ArrayList<>();
        this.context = context;
        this.items = items;
        this.fragment = fragment;
        this.itemViewModel = itemViewModel;
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
        OccasionModel occasionModel = items.get(position);
        holder.mItem = items.get(position);

        itemViewModel.getOccasionTotalCost(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), totalCost -> {
            if(totalCost != null){
                double cost = totalCost.doubleValue();
                holder.textView_list_history_sum_of_items_value.setText(Double.toString(cost));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(occasionModel);
                dialogFragment.show(fragment.getChildFragmentManager(), "Test");
            }
        });

        itemViewModel.getOccasionItems(occasionModel.getID()).observe(fragment.getViewLifecycleOwner(), items -> {
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
            if(((List) results.values).size() != 0) {
                items.clear();
                items.addAll((List) results.values);
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