package com.cashhub.android.Interface;

import android.net.Uri;

import com.cashhub.android.Models.AllOffersDataModel;
import com.cashhub.android.Models.AppOpenModel;
import com.cashhub.android.Models.BestOfferDetailsModel;
import com.cashhub.android.Models.InviteDataModel;
import com.cashhub.android.Models.MoreTaskCoinModel;
import com.cashhub.android.Models.MoreTaskListModel;
import com.cashhub.android.Models.OfferClickedModel;
import com.cashhub.android.Models.OfferClickedModelShopping;
import com.cashhub.android.Models.OfferDetailsModel;
import com.cashhub.android.Models.OfferListModel;
import com.cashhub.android.Models.PayOutDataModel;
import com.cashhub.android.Models.UserProfileModel;
import com.cashhub.android.Models.UserSignUpModel;
import com.cashhub.android.Models.UserTransactionModel;
import com.cashhub.android.Models.WalletRedeemDataModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetApiData {

    @FormUrlEncoded
    @POST("userSignup")
    Call<UserSignUpModel> getAllUsers(
            @Field("deviceId") String deviceId,    //requires
            @Field("deviceName") String deviceName,
            @Field("socialType") String socialType,  //requires
            @Field("socialId") String socialId,  //requires
            @Field("socialEmail") String socialEmail,
            @Field("socialName") String socialName,
            @Field("socialImgurl") Uri socialImgurl,
            @Field("versionName") String versionName,  //requires
            @Field("versionCode") int versionCode,
            @Field("utmSource") String utmSource,
            @Field("utmMedium") String utmMedium,
            @Field("advertisingId") String advertisingId,
            @Field("fcmToken") String fcmToken,
            @Field("referalUrl") String referalUrl,
            @Field("socialToken") String socialToken
            );  //requires


    @FormUrlEncoded
    @POST("appOpen")
    Call<AppOpenModel> getAppOpenDetails(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode
    );

    @FormUrlEncoded
    @POST("offerList")
    Call<OfferListModel> getOfferWallList(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode);

    @FormUrlEncoded
    @POST("offerDetails")
    Call<OfferDetailsModel> getOfferDetails(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode,
            @Field("offerId") String offerId);

    @FormUrlEncoded
    @POST("offerClicked")
    Call<OfferClickedModel> getOfferClickedDetails(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode,
            @Field("offerId") String offerId);

    @FormUrlEncoded
    @POST("inviteData")
    Call<InviteDataModel> getInviteData(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode);

    @FormUrlEncoded
    @POST("moreTaskList")
    Call<MoreTaskListModel> getMoreTaskListData(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode);


    @FormUrlEncoded
    @POST("moreTaskCoin")
    Call<MoreTaskCoinModel> getMoreTaskCoin(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode,
            @Field("moreTaskId") int moreTaskId);

    @FormUrlEncoded
    @POST("userProfile")
    Call<UserProfileModel> getUserProfileData(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode,
            @Field("actionType") String actionType
    );

    @FormUrlEncoded
    @POST("userProfile")
    Call<UserProfileModel> sendUserProfileData(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode,
            @Field("actionType") String actionType,
            @Field("userName") String userName,
            @Field("userEmail") String userEmail,
            @Field("mobileNumber") String mobileNumber,
            @Field("gender") String gender,
            @Field("location") String location,
            @Field("occupation") String occupation,
            @Field("birthDate") String birthDate
    );

    @FormUrlEncoded
    @POST("userTransactions")
    Call<UserTransactionModel> getUserTransactions(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode);
@FormUrlEncoded
    @POST("userSignIn")
    Call<UserSignUpModel> getUserLogin(
        @Field("email") String email,
        @Field("password") String password,
        @Field("versionName") String versionName,
        @Field("versionCode") int versionCode);

    @FormUrlEncoded
    @POST("rewardList")
    Call<PayOutDataModel> getPayoutData(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode);


    @FormUrlEncoded
    @POST("walletRedeem")
    Call<WalletRedeemDataModel> sendWithdrawCoinsData(
            @Field("userId") int userId,
            @Field("securityToken") String securityToken,
            @Field("versionName") String versionName,
            @Field("versionCode") int versionCode,
            @Field("paytmNumber") String paytmNumber,
            @Field("payEmail") String payEmail,
            @Field("payAmount") String payAmount,
            @Field("redeemType") String redeemType);

    @POST("chOfferList")
    @FormUrlEncoded
    Call<AllOffersDataModel> getOfferlist(@Field("userId") int userId,
                                          @Field("securityToken") String securityToken,
                                          @Field("versionName") String versionName,
                                          @Field("versionCode") int versionCode,
                                          @Field("userFrom") String userFrom);




    @FormUrlEncoded
    @POST("chOfferDetails")
    Call<BestOfferDetailsModel> viewofferDetails(@Field("userId") int userId,
                                                 @Field("securityToken") String securityToken,
                                                 @Field("versionName") String versionName,
                                                 @Field("versionCode") int versionCode,
                                                 @Field("offerId") String offerid,
                                                 @Field("userFrom") String userFrom);


    @FormUrlEncoded
    @POST("chOfferClicked")
    Call<OfferClickedModelShopping> offerclicked(@Field("userId") int userId,
                                                 @Field("securityToken") String securityToken,
                                                 @Field("versionName") String versionName,
                                                 @Field("versionCode") int versionCode,
                                                 @Field("offerId") String offerid,
                                                 @Field("userFrom") String userFrom,
                                                 @Field("advertisingId") String advertisingId);




}
