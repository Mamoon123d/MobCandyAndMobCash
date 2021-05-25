package com.cashhub.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashhub.android.Models.Transaction;
import com.cashhub.android.R;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    ArrayList<Transaction> transactionsDetails;
    Context context;

    public TransactionsAdapter(ArrayList<Transaction> items, Context context) {
        this.transactionsDetails = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trans_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction transaction = transactionsDetails.get(position);
        holder.transactionsName.setText(transaction.getTransName());
        // holder.transactiontime.setText(transaction.getTransTime());
        holder.transamount.setText(transaction.getAmount());
        holder.transactiondate.setText(transaction.getTransTime() + " , " + transaction.getTransDate());

    }

    @Override
    public int getItemCount() {
        return transactionsDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transactionsName, transamount, transactiondate, transactiontime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionsName = itemView.findViewById(R.id.tr_title);
            transamount = itemView.findViewById(R.id.tr_coinsTv);
            transactiondate = itemView.findViewById(R.id.tr_subtitle);
            // transactiontime = itemView.findViewById(R.id.transactiontime);
        }
    }
}
