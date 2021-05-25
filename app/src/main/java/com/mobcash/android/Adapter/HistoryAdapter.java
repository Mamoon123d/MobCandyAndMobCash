package com.mobcash.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.PayoutHistory;
import com.mobcash.android.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    ArrayList<PayoutHistory> payoutHistoryArrayList;
    Context context;

    public HistoryAdapter(ArrayList<PayoutHistory> payoutHistoryArrayList, Context context) {
        this.payoutHistoryArrayList = payoutHistoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        final PayoutHistory payoutHistory = payoutHistoryArrayList.get(position);
        holder.title.setText(payoutHistory.getTitle());
        holder.description.setText(payoutHistory.getShortTxt());

    }

    @Override
    public int getItemCount() {
        return payoutHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.descriptions);


        }
    }
}
