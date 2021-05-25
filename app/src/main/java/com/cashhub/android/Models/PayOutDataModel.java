package com.cashhub.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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


    @SerializedName("rewardList")
    @Expose
    private List<Reward> rewardList;


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

    public List<Reward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }

    public static class Reward {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("imageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("rewardType")
        @Expose
        private String rewardType;
        @SerializedName("payout_value")
        @Expose
        private ArrayList<String> payoutValue;
        @SerializedName("payout_reward")
        @Expose
        private List<String> payoutReward;
        @SerializedName("redeemLimit")
        @Expose
        private String redeemLimit;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getRewardType() {
            return rewardType;
        }

        public void setRewardType(String rewardType) {
            this.rewardType = rewardType;
        }

        public ArrayList<String> getPayoutValue() {
            return payoutValue;
        }

        public void setPayoutValue(ArrayList<String> payoutValue) {
            this.payoutValue = payoutValue;
        }

        public List<String> getPayoutReward() {
            return payoutReward;
        }

        public void setPayoutReward(List<String> payoutReward) {
            this.payoutReward = payoutReward;
        }

        public String getRedeemLimit() {
            return redeemLimit;
        }

        public void setRedeemLimit(String redeemLimit) {
            this.redeemLimit = redeemLimit;
        }

    }

}