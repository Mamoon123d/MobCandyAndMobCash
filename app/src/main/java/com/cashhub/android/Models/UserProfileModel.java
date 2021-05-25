package com.cashhub.android.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileModel {

@SerializedName("message")
@Expose
private String message;
@SerializedName("status")
@Expose
private Integer status;
@SerializedName("userName")
@Expose
private String userName;
@SerializedName("userEmail")
@Expose
private String userEmail;
@SerializedName("mobileNumber")
@Expose
private String mobileNumber;
@SerializedName("gender")
@Expose
private String gender;
@SerializedName("location")
@Expose
private String location;
@SerializedName("occupation")
@Expose
private String occupation;
@SerializedName("birthDate")
@Expose
private String birthDate;
@SerializedName("socialImgurl")
@Expose
private String socialImgurl;

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

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getUserEmail() {
return userEmail;
}

public void setUserEmail(String userEmail) {
this.userEmail = userEmail;
}

public String getMobileNumber() {
return mobileNumber;
}

public void setMobileNumber(String mobileNumber) {
this.mobileNumber = mobileNumber;
}

public String getGender() {
return gender;
}

public void setGender(String gender) {
this.gender = gender;
}

public String getLocation() {
return location;
}

public void setLocation(String location) {
this.location = location;
}

public String getOccupation() {
return occupation;
}

public void setOccupation(String occupation) {
this.occupation = occupation;
}

public String getBirthDate() {
return birthDate;
}

public void setBirthDate(String birthDate) {
this.birthDate = birthDate;
}

public String getSocialImgurl() {
return socialImgurl;
}

public void setSocialImgurl(String socialImgurl) {
this.socialImgurl = socialImgurl;
}

}