package com.mobcash.android.Adapter;

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

import com.mobcash.android.Models.Offer;
import com.mobcash.android.Services.sharedConstant;
import com.mobcash.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularOfferListAdapter extends RecyclerView.Adapter<PopularOfferListAdapter.ViewHolder> {

    ArrayList<Offer> offers;
    Context context;
    Button goToOffer,button_close;
    TextView appTitle,details_steps;
    ImageView appImage1;
    private ProgressDialog progressDialog;
    String TAG = "testing";
    private final OnItemClickListener listener;

    public PopularOfferListAdapter(ArrayList<Offer> offers, Context context,OnItemClickListener listener) {
        this.offers = offers;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Offer Offer);
    }

    @NonNull
    @Override
    public PopularOfferListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_offer_list_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularOfferListAdapter.ViewHolder holder, int position) {

        final Offer offer = offers.get(position);

        String truncateString = offer.getDescription();
        final String offerID = String.valueOf(offer.getOfferId());
        holder.appTitle.setText(offer.getOfferName());
        holder.descriptions.setText(truncateString.substring(0,85));
        holder.offerAmount.setText(sharedConstant.getSharedPreferenceString(context, "Currency", "")+" "+offer.getOfferAmount());


        Picasso.get().load(offer.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).centerCrop()
                .resize(100,100)
                .into(holder.appImage);



        holder.bind(offers.get(position), listener);


    }


    @Override
    public int getItemCount() {
        return offers.size();
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

        public void bind(final Offer item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }




}
