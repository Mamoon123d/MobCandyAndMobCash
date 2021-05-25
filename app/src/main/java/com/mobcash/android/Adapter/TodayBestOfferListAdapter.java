package com.mobcash.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.BestOfferDatum;
import com.mobcash.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TodayBestOfferListAdapter extends RecyclerView.Adapter<TodayBestOfferListAdapter.ViewHolder> {
    ArrayList<BestOfferDatum> items;

    Context context;

    public TodayBestOfferListAdapter(ArrayList<BestOfferDatum> bestOfferDatum, Context context) {
        this.context = context;
        this.items = bestOfferDatum;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shoping_offer_list_adapter_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.offername.setText(items.get(position).getOfferName());
        String truncateString = items.get(position).getShortDescription();
        if(truncateString.length()<85) holder.description.setText(items.get(position).getShortDescription());
        else  holder.description.setText(truncateString.substring(0,85));

        holder.offercashback.setText(items.get(position).getCashBack());
        holder.offercategory.setText(items.get(position).getCategory());
        holder.tv_success.setText(items.get(position).getSuces()+"% Success");
        holder.tv_user_visit.setText(items.get(position).getUsrs()+" Users Today");

        Picasso.get().load(items.get(position).getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into((holder.offerImage));

    }

    @Override
    public int getItemCount() {
        if(items!=null){
            return items.size();
        }
        return 0;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView offername,offercategory,description,offercashback,tv_success,tv_user_visit;
        ImageView offerImage;

        public ViewHolder(View itemView) {
            super(itemView);
            offername = itemView.findViewById(R.id.offer_name);
            offercategory = itemView.findViewById(R.id.offer_category);
            description = itemView.findViewById(R.id.offer_short_description);
            offercashback = itemView.findViewById(R.id.offer_cashback);
            offerImage = itemView.findViewById(R.id.offer_image);
            tv_success = itemView.findViewById(R.id.tv_success);
            tv_user_visit = itemView.findViewById(R.id.tv_user_visit);

        }

    }


}