package com.mobcash.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.Transaction;
import com.mobcash.android.R;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_transactions_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction transaction = transactionsDetails.get(position);
        holder.transactionsName.setText(transaction.getTransName());
        holder.transactiondate.setText(transaction.getTransDate());
        holder.transactiontime.setText(transaction.getTransTime());
        holder.transamount.setText(transaction.getAmount());

    }

    @Override
    public int getItemCount() {
        return transactionsDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView transactionsName, transamount, transactiondate, transactiontime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionsName = itemView.findViewById(R.id.transactionsName);
            transamount = itemView.findViewById(R.id.transamount);
            transactiondate = itemView.findViewById(R.id.transactiondate);
            transactiontime = itemView.findViewById(R.id.transactiontime);
        }
    }
}
