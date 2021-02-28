package com.example.paybuddy.PhoneCall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.view.View.OnClickListener;

import com.example.paybuddy.Models.Contact;
import com.example.paybuddy.Models.HistoryModel;
import com.example.paybuddy.R;

public class HistoryLogAdapter extends RecyclerView.Adapter<HistoryLogAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<HistoryModel> callingHistory;
    private List<HistoryModel> filteredItems;

    public HistoryLogAdapter(Context context) {
        Log.d("CREATED_ADAPTER", "...");

        this.context = context;
        this.filteredItems = new ArrayList<>();
    }

    public void addItems(List<HistoryModel> items) {
        this.callingHistory = items;
        this.filteredItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryLogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("CALL_HISTORY_INFLATED", "...");
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_call_history_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryLogAdapter.MyViewHolder holder, int position) {
        Log.d("BINDING", "...");
        HistoryModel currentHistoryModel = callingHistory.get(position);
        holder.textViewDate.setText(currentHistoryModel.getDate());
        holder.time.setText(currentHistoryModel.getTime());
        holder.textViewNr.setText(currentHistoryModel.getNumber());
        holder.type.setText(currentHistoryModel.getType());
        holder.callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INDEX:" ,"Clicked index: " + position);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentHistoryModel.getNumber()));
                context.startActivity(intent);
            }
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<HistoryModel> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(filteredItems);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

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

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            callingHistory.clear();
            callingHistory.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    /**
     * This class will represent our holder of values passed from our custom HistoryModel.
     */
    public class  MyViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView textViewNr, textViewDate, type, time;
        private Button callButton;

        /**
         * We fetch all the UI components from the view.
         * @param itemView a view object representing 1 of the items in the adapter.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            time = (TextView) itemView.findViewById(R.id.textViewCallTime);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDateCall);
            textViewNr = (TextView) itemView.findViewById(R.id.textViewNr);
            callButton = (Button) itemView.findViewById(R.id.callBtn);
            type = (TextView) itemView.findViewById(R.id.valueType);
        }
    }
}
