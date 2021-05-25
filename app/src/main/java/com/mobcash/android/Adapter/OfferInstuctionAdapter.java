package com.mobcash.android.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.PayoutStep;
import com.mobcash.android.R;

import java.util.ArrayList;

public class OfferInstuctionAdapter extends RecyclerView.Adapter<OfferInstuctionAdapter.ViewHolder> {

    ArrayList<PayoutStep> items;
    Context context;

    public OfferInstuctionAdapter(ArrayList<PayoutStep> items, Context context) {
        this.items = items;
        this.context = context;

    }


    @NonNull
    @Override
    public OfferInstuctionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_steps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PayoutStep instruction = items.get(position);
        Log.e("size", "onBindViewHolder: "+instruction );
        holder.propertyName.setText(instruction.getPropertyName());
        holder.propertyValue.setText("₹ "+instruction.getPropertyValue());
        Log.e("size", "OfferInstuctionAdapter: "+items.size() );
        Log.e("size", "onBindViewHolder: "+instruction.getPropertyValue() );
        if(position%2==0){
            holder.propertyName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.download_icon, 0, 0, 0);
        }else{
            holder.propertyName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.smartphone, 0, 0, 0);
        }

    }

    @Override
    public int getItemCount() {
       if(items!=null) {
           return items.size();
       }
       return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView propertyName, propertyValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyValue = itemView.findViewById(R.id.propertyValue);
            propertyName = itemView.findViewById(R.id.propertyName);

        }
    }
}