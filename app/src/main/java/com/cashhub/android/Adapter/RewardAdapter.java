package com.cashhub.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cashhub.android.Models.RewardListData;
import com.cashhub.android.R;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyHolder> {
    Context context;
    List<RewardListData.Reward> list;
    private static ClickListener clickListener;


    public RewardAdapter(Context context, List<RewardListData.Reward> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cashout_view_layout, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

         RewardListData.Reward payoutArray = list.get(position);
        holder.title.setText(payoutArray.getTitle());
       // holder.description.setText(payoutArray.getShortTxt());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.paytmTitle);
            description = itemView.findViewById(R.id.paytmDesc);

        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}