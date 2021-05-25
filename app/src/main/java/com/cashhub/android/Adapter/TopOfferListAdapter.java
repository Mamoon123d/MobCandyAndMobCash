package com.cashhub.android.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashhub.android.Models.TopOffer;
import com.cashhub.android.Services.sharedConstant;
import com.cashhub.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopOfferListAdapter extends RecyclerView.Adapter<TopOfferListAdapter.ViewHolder> {

    ArrayList<TopOffer> topOffers;
    Context context;
    Button goToOffer,button_close;
    TextView appTitle,details_steps;
    ImageView appImage1;
    private ProgressDialog progressDialog;
    String TAG = "testing";

//    private static ClickListener clickListener;


    private final OnItemClickListener listener;
    int positionAd;


    public TopOfferListAdapter(ArrayList topOffers, Context context,OnItemClickListener listener) {
        this.topOffers = topOffers;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TopOffer topOffer);
    }

    @NonNull
    @Override
    public TopOfferListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_offer_of_the_day_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TopOfferListAdapter.ViewHolder holder, int position) {

        positionAd = position;

        final TopOffer topOffer = topOffers.get(position);

        String truncateString = topOffer.getDescription();
        final String offerID = String.valueOf(topOffer.getOfferId());
        holder.appTitle.setText(topOffer.getOfferName());
        if(truncateString.length()<85){
            holder.descriptions.setText(truncateString);
        }
        else holder.descriptions.setText(truncateString.substring(0,85));

        holder.offerAmount.setText(sharedConstant.getSharedPreferenceString(context, "Currency", "")+" "+topOffer.getOfferAmount());

        Picasso.get().load(topOffer.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).centerCrop()
                .resize(100,100)
                .into(holder.appImage);

        holder.bind(topOffers.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return topOffers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appTitle,descriptions,offerAmount;
        ImageView appImage;

        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            appTitle = itemView.findViewById(R.id.appTitle);
            descriptions = itemView.findViewById(R.id.descriptions);
            offerAmount = itemView.findViewById(R.id.offerAmount);

            appImage = itemView.findViewById(R.id.appImage);
            linearLayout = itemView.findViewById(R.id.linearLayout);


        }


        public void bind(final TopOffer item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }



    }



}
