package com.mobcash.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PayOutDataModel {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("paycoinLimit")
    @Expose
    private Integer paycoinLimit;
    @SerializedName("userCoin")
    @Expose
    private String userCoin;
    @SerializedName("userAmount")
    @Expose
    private String userAmount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("todayCoin")
    @Expose
    private Integer todayCoin;
    @SerializedName("weekCoin")
    @Expose
    private Integer weekCoin;
    @SerializedName("transText")
    @Expose
    private String transText;
    @SerializedName("payoutValues")
    @Expose
    private ArrayList<String> payoutValues = null;
    @SerializedName("payoutArray")
    @Expose
    private ArrayList<PayoutArray> payoutArray = null;
    @SerializedName("arrFlag")
    @Expose
    private Boolean arrFlag;
    @SerializedName("payoutHistory")
    @Expose
    private ArrayList<PayoutHistory> payoutHistory = null;

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

    public Integer getPaycoinLimit() {
        return paycoinLimit;
    }

    public void setPaycoinLimit(Integer paycoinLimit) {
        this.paycoinLimit = paycoinLimit;
    }

    public String getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(String userCoin) {
        this.userCoin = userCoin;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTodayCoin() {
        return todayCoin;
    }

    public void setTodayCoin(Integer todayCoin) {
        this.todayCoin = todayCoin;
    }

    public Integer getWeekCoin() {
        return weekCoin;
    }

    public void setWeekCoin(Integer weekCoin) {
        this.weekCoin = weekCoin;
    }

    public String getTransText() {
        return transText;
    }

    public void setTransText(String transText) {
        this.transText = transText;
    }

    public ArrayList<String> getPayoutValues() {
        return payoutValues;
    }

    public void setPayoutValues(ArrayList<String> payoutValues) {
        this.payoutValues = payoutValues;
    }

    public ArrayList<PayoutArray> getPayoutArray() {
        return payoutArray;
    }

    public void setPayoutArray(ArrayList<PayoutArray> payoutArray) {
        this.payoutArray = payoutArray;
    }

    public Boolean getArrFlag() {
        return arrFlag;
    }

    public void setArrFlag(Boolean arrFlag) {
        this.arrFlag = arrFlag;
    }

    public ArrayList<PayoutHistory> getPayoutHistory() {
        return payoutHistory;
    }

    public void setPayoutHistory(ArrayList<PayoutHistory> payoutHistory) {
        this.payoutHistory = payoutHistory;
    }

}