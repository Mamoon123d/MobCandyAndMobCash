package com.cashhub.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferDetails {

    @SerializedName("offerId")
    @Expose
    private Integer offerId;
    @SerializedName("offerAmount")
    @Expose
    private String offerAmount;
    @SerializedName("offerName")
    @Expose
    private String offerName;
    @SerializedName("payoutType")
    @Expose
    private String payoutType;
    @SerializedName("offerType")
    @Expose
    private String offerType;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("detailSteps")
    @Expose
    private String detailSteps;
    @SerializedName("descriptionLoc")
    @Expose
    private String descriptionLoc;
    @SerializedName("payoutSteps")
    @Expose
    private ArrayList<PayoutStep> payoutSteps = null;

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getPayoutType() {
        return payoutType;
    }

    public void setPayoutType(String payoutType) {
        this.payoutType = payoutType;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailSteps() {
        return detailSteps;
    }

    public void setDetailSteps(String detailSteps) {
        this.detailSteps = detailSteps;
    }

    public String getDescriptionLoc() {
        return descriptionLoc;
    }

    public void setDescriptionLoc(String descriptionLoc) {
        this.descriptionLoc = descriptionLoc;
    }

    public ArrayList<PayoutStep> getPayoutSteps() {
        return payoutSteps;
    }

    public void setPayoutSteps(ArrayList<PayoutStep> payoutSteps) {
        this.payoutSteps = payoutSteps;
    }

}