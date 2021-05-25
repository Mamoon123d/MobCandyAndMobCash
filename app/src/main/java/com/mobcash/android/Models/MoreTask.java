package com.mobcash.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreTask {

    @SerializedName("moreTaskId")
    @Expose
    private Integer moreTaskId;
    @SerializedName("taskAmount")
    @Expose
    private String taskAmount;
    @SerializedName("taskTitle")
    @Expose
    private String taskTitle;
    @SerializedName("actionUrl")
    @Expose
    private String actionUrl;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("taskDescription")
    @Expose
    private String taskDescription;

    public Integer getMoreTaskId() {
        return moreTaskId;
    }

    public void setMoreTaskId(Integer moreTaskId) {
        this.moreTaskId = moreTaskId;
    }

    public String getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(String taskAmount) {
        this.taskAmount = taskAmount;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

}