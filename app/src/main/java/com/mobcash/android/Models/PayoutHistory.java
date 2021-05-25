package com.mobcash.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayoutHistory {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("title")
@Expose
private String title;
@SerializedName("shortTxt")
@Expose
private String shortTxt;

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

public String getShortTxt() {
return shortTxt;
}

public void setShortTxt(String shortTxt) {
this.shortTxt = shortTxt;
}

}