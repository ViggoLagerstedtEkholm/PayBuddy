package com.example.paybuddy.Occasions.List_of_occasions;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybuddy.Home.UpdateViewModel;
import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Occasions.Dialogs.DialogPreviewOccasion;
import com.example.paybuddy.R;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    private List<OccasionModel> items;
    private final Fragment currentFragment;
    private UpdateViewModel updateViewModel;

    public MyItemRecyclerViewAdapter(List<OccasionModel> items, Fragment currentFragment) {
        this.items = items;
        this.currentFragment = currentFragment;
        updateViewModel = new ViewModelProvider(currentFragment.getActivity()).get(UpdateViewModel.class);

    }

    public void addItems(List<OccasionModel> occasionModels){
        this.items = occasionModels;
        Log.d("Searching", "...");
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

        for(ItemModel item : occasionModel.getItems()){
            cost += item.getPrice() * item.getQuantity();
        }

        holder.titleOfMyOccasion.setText(occasionModel.getDescription());
        holder.textViewDateOccasionCard.setText(occasionModel.getDate());

        holder.textViewSumOfItemsOccasionCard.setText(Double.toString(cost));
        holder.textViewPeopleOccasionCard.setText("TODO");

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                MainActivity.databaseHelper.delete(occasionModel);
                updateViewModel.updateTotalPrice(MainActivity.databaseHelper.getSumItems());
                updateViewModel.updateTotalOccasion(MainActivity.databaseHelper.getAmountOfOccasion());
                notifyDataSetChanged();
            }
        });

        holder.buttonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewOccasion dialogFragment = new DialogPreviewOccasion(occasionModel);
                dialogFragment.show(currentFragment.getChildFragmentManager(), "Test");
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleOfMyOccasion;
        public final TextView textViewDateOccasionCard;
        public final TextView textViewSumOfItemsOccasionCard;
        public final TextView textViewPeopleOccasionCard;
        public final Button buttonRemove;
        public final Button buttonPreview;
        public OccasionModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            buttonRemove = (Button) view.findViewById(R.id.buttonRemoveItem);
            buttonPreview = (Button) view.findViewById(R.id.buttonPreview);

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