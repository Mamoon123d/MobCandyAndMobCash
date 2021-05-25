package com.cashhub.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppOpenModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("forceUpdate")
    @Expose
    private Boolean forceUpdate;
    @SerializedName("paycoinLimit")
    @Expose
    private Integer paycoinLimit;
    @SerializedName("userCoin")
    @Expose
    private Integer userCoin;
    @SerializedName("userAmount")
    @Expose
    private Double userAmount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("attendenceId")
    @Expose
    private Integer attendenceId;
    @SerializedName("attendence")
    @Expose
    private Boolean attendence;
    @SerializedName("attendenceCoin")
    @Expose
    private Integer attendenceCoin;
    @SerializedName("waitTime")
    @Expose
    private String waitTime;
    @SerializedName("watchVideoId")
    @Expose
    private Integer watchVideoId;
    @SerializedName("packAge")
    @Expose
    private String packAge;

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

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Integer getPaycoinLimit() {
        return paycoinLimit;
    }

    public void setPaycoinLimit(Integer paycoinLimit) {
        this.paycoinLimit = paycoinLimit;
    }

    public Integer getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(Integer userCoin) {
        this.userCoin = userCoin;
    }

    public Double getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(Double userAmount) {
        this.userAmount = userAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAttendenceId() {
        return attendenceId;
    }

    public void setAttendenceId(Integer attendenceId) {
        this.attendenceId = attendenceId;
    }

    public Boolean getAttendence() {
        return attendence;
    }

    public void setAttendence(Boolean attendence) {
        this.attendence = attendence;
    }

    public Integer getAttendenceCoin() {
        return attendenceCoin;
    }

    public void setAttendenceCoin(Integer attendenceCoin) {
        this.attendenceCoin = attendenceCoin;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public Integer getWatchVideoId() {
        return watchVideoId;
    }

    public void setWatchVideoId(Integer watchVideoId) {
        this.watchVideoId = watchVideoId;
    }

    public String getPackAge() {
        return packAge;
    }

    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }
}