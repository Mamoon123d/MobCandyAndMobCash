package com.mobcash.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoreTaskListModel {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("watchCoin")
    @Expose
    private Integer watchCoin;
    @SerializedName("reviewTask")
    @Expose
    private String reviewTask;
    @SerializedName("moreTasks")
    @Expose
    private ArrayList<MoreTask> moreTasks = null;

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

    public Integer getWatchCoin() {
        return watchCoin;
    }

    public void setWatchCoin(Integer watchCoin) {
        this.watchCoin = watchCoin;
    }

    public String getReviewTask() {
        return reviewTask;
    }

    public void setReviewTask(String reviewTask) {
        this.reviewTask = reviewTask;
    }

    public ArrayList<MoreTask> getMoreTasks() {
        return moreTasks;
    }

    public void setMoreTasks(ArrayList<MoreTask> moreTasks) {
        this.moreTasks = moreTasks;
    }

}