package com.example.paybuddy.PhoneCall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.paybuddy.Models.HistoryModel;
import com.example.paybuddy.R;

/**
 *  This adapter shows the call history of the user's phone. We also implement Filterable to filter
 *  the List.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class HistoryLogRecyclerViewAdapter extends RecyclerView.Adapter<HistoryLogRecyclerViewAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private List<HistoryModel> callingHistory;
    private List<HistoryModel> filteredItems;

    public HistoryLogRecyclerViewAdapter(Context context) {
        this.context = context;
        this.filteredItems = new ArrayList<>();
    }

    /**
     * Adds items to our items array.
     * @param items List<HistoryModel>
     */
    public void addItems(List<HistoryModel> items) {
        this.callingHistory = items;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    /**
     * Inflate our ViewHolder with "fragment_call_history_list_item.xml".
     * @param parent view that contains other views.
     * @param viewType int
     * @return HistoryLogAdapter.MyViewHolder
     */
    @NonNull
    @Override
    public HistoryLogRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_call_history_list_item, parent, false));
    }

    /**
     * This method will be called for all items in our RecyclerView.
     * @param holder the holder object.
     * @param position the position the item has in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryLogRecyclerViewAdapter.MyViewHolder holder, int position) {
        HistoryModel currentHistoryModel = callingHistory.get(position);
        holder.textViewDate.setText(currentHistoryModel.getDate());
        holder.time.setText(currentHistoryModel.getTime());
        holder.textViewNr.setText(currentHistoryModel.getNumber());
        holder.type.setText(currentHistoryModel.getType());

        //Calls the number on the event.
        holder.callButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentHistoryModel.getNumber()));
            context.startActivity(intent);
        });
    }

    /**
     * This method returns the amount of items in our call history.
     * @Override we specify specific behavior.
     * @return int
     */
    @Override
    public int getItemCount() {
        return callingHistory == null ? 0 : callingHistory.size();
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
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HistoryModel> filteredList = new ArrayList<>();

            //Check if empty.
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(filteredItems);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                //Try to find a match.
                for(HistoryModel item : filteredItems){
                    if(item.getNumber().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        /**
         * When we have performed a search this method will be called.
         * @param constraint a sequence of characters.
         * @param results the list of results.
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(callingHistory != null){
                callingHistory.clear();
                callingHistory.addAll(((List<HistoryModel>)results.values));
                notifyDataSetChanged();
            }
        }
    };

    /**
     * This class will represent our holder of values passed from our custom HistoryModel.
     */
    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewNr;
        private final TextView textViewDate;
        private final TextView type;
        private final TextView time;
        private final Button callButton;

        /**
         * We fetch all the UI components from the view.
         * @param itemView a view object representing 1 of the items in the adapter.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textViewCallTime);
            textViewDate = itemView.findViewById(R.id.textViewDateCall);
            textViewNr = itemView.findViewById(R.id.textViewNr);
            callButton = itemView.findViewById(R.id.callBtn);
            type = itemView.findViewById(R.id.valueType);
        }
    }
}
