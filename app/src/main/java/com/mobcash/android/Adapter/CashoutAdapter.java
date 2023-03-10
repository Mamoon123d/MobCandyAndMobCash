package com.mobcash.android.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.PayoutArray;
import com.mobcash.android.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CashoutAdapter extends RecyclerView.Adapter<CashoutAdapter.ViewHolder>  {

    private static ClickListener clickListener;
   int positionAd;

    ArrayList<PayoutArray> payoutArraysList;
    Context context;

    public CashoutAdapter(ArrayList<PayoutArray> payoutArraysList, Context context) {
        this.payoutArraysList = payoutArraysList;
        this.context = context;
    }

    @NonNull
    @Override
    public CashoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cashout_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CashoutAdapter.ViewHolder holder, int position) {
        positionAd = position;

        Log.e(TAG, "onBindViewHolder: "+positionAd );
        final PayoutArray payoutArray = payoutArraysList.get(position);
        holder.title.setText(payoutArray.getTitle());
        holder.description.setText(payoutArray.getShortTxt());

    }

    @Override
    public int getItemCount() {
      return payoutArraysList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,description;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.paytmTitle);
            description = itemView.findViewById(R.id.paytmDesc);


        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(positionAd, v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CashoutAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
