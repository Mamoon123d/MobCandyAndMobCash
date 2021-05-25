package com.cashhub.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferListModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("topOffer")
    @Expose
    private ArrayList<TopOffer> topOffer = null;
    @SerializedName("offers")
    @Expose
    private ArrayList<Offer> offers = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<TopOffer> getTopOffer() {
        return topOffer;
    }

    public void setTopOffer(ArrayList<TopOffer> topOffer) {
        this.topOffer = topOffer;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

}