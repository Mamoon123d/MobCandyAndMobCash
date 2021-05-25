package com.cashhub.android.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;

import com.applovin.sdk.AppLovinSdk;
import com.cashhub.android.Activities.OfferDetailsActivityNotification;
import com.cashhub.android.Interface.GetApiData;
import com.cashhub.android.Models.MoreTaskCoinModel;
import com.cashhub.android.Models.Offer;
import com.cashhub.android.Models.OfferListModel;
import com.cashhub.android.Models.TopOffer;
import com.cashhub.android.Services.RetrofitClient;
import com.cashhub.android.Services.sharedConstant;
import com.cashhub.android.Adapter.PopularOfferListAdapter;
import com.cashhub.android.Adapter.TopOfferListAdapter;
import com.cashhub.android.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment implements View.OnClickListener {

    protected FragmentActivity mActivity;
    //    app lovin
    private AppLovinIncentivizedInterstitial myIncent;
   private LinearLayout topOffer;

   RecyclerView topOfferRecycler,popularOffersListRecycler;
    private ProgressDialog progressDialog;
    PopularOfferListAdapter popularOfferListAdapter;
    TopOfferListAdapter topOfferListAdapter;
    LinearLayout attendanceButtonLayout;
    TextView attendanceRewardsText;
    String TAG ="testing",waitTime;
    private boolean attendanceAllowed;
    ArrayList<Offer> offerArrayList;
    ArrayList<TopOffer> topOfferArrayList;
    Button goToOffer,button_close;
    TextView appTitle,details_steps;
    ImageView appImage1;
    AVLoadingIndicatorView avLoadingIndicatorView;

    public TasksFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        topOffer = view.findViewById(R.id.topOffer);
        attendanceButtonLayout = view.findViewById(R.id.AttendanceButtonLayout);
        attendanceRewardsText = view.findViewById(R.id.attendanceRewardsText);
        popularOffersListRecycler = view.findViewById(R.id.popularOffersListRecycler);
        topOfferRecycler = view.findViewById(R.id.topOfferRecycler);
        avLoadingIndicatorView = view.findViewById(R.id.loader_indicator);
        attendanceAllowed =  sharedConstant.getSharedPreferenceBoolean(mActivity, "Attendence",false);

        waitTime = sharedConstant.getSharedPreferenceString(mActivity, "WaitTime", "");

        attendanceRewardsText.setText(sharedConstant.getSharedPreferenceString(mActivity, "AttendenceCoin","" )+" "+"Coins");

        AppLovinSdk.initializeSdk(mActivity);
        // Create and preload a rewarded video soon after initialization.
        myIncent = AppLovinIncentivizedInterstitial.create(mActivity);
        myIncent.preload(null);


        getOfferWallList();

        attendanceButtonLayout.setOnClickListener(this);

        return view;
    }


    private void getOfferWallList() {
        avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<OfferListModel> call = getApiData.getOfferWallList(sharedConstant.getSharedPreferenceInt(mActivity, "userId", 0)
                , sharedConstant.getSharedPreferenceString(mActivity, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(mActivity, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(mActivity, "versionCode", 0));

        call.enqueue(new Callback<OfferListModel>() {
            @Override
            public void onResponse(Call<OfferListModel> call, Response<OfferListModel> response) {
                dismissProgressDialog();

                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            offerArrayList = response.body().getOffers();
                            topOfferArrayList = response.body().getTopOffer();
                            //Popular offers click or adapter
                            popularOfferListAdapter = new PopularOfferListAdapter(offerArrayList, mActivity, new PopularOfferListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(final Offer Offer) {

                                    Intent intent = new Intent(mActivity, OfferDetailsActivityNotification.class);
                                    intent.putExtra("offerId",+Offer.getOfferId());
                                    mActivity.startActivity(intent);

                                }
                            });



                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                            popularOffersListRecycler.setLayoutManager(mLayoutManager);
                            popularOffersListRecycler.setItemAnimator(new DefaultItemAnimator());
                            popularOffersListRecycler.setAdapter(popularOfferListAdapter);

                            //topOfferListAdapter offers click or adapter
                            topOfferListAdapter = new TopOfferListAdapter(topOfferArrayList, mActivity, new TopOfferListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(final TopOffer topOffer) {
                                    Intent intent = new Intent(mActivity, OfferDetailsActivityNotification.class);
                                    intent.putExtra("offerId",+topOffer.getOfferId());
//                                    intent.putExtra("offerId",offerID);
                                    mActivity.startActivity(intent);
                                }
                            });
                            RecyclerView.LayoutManager mmLayoutManager = new LinearLayoutManager(mActivity);
                            topOfferRecycler.setLayoutManager(mmLayoutManager);
                            topOfferRecycler.setItemAnimator(new DefaultItemAnimator());
                            topOfferRecycler.setAdapter(topOfferListAdapter);

                        } else {
                            Toast.makeText(mActivity, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(mActivity, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OfferListModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onFailure: "+t );
//                Toast.makeText(mActivity, "System Message: " +t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMoreTaskCoin() {
        avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<MoreTaskCoinModel> call = getApiData.getMoreTaskCoin(sharedConstant.getSharedPreferenceInt(mActivity, "userId", 0)
                , sharedConstant.getSharedPreferenceString(mActivity, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(mActivity, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(mActivity, "versionCode", 0), sharedConstant.getSharedPreferenceInt(mActivity, "AttendenceId", 0));


        call.enqueue(new Callback<MoreTaskCoinModel>() {
            @Override
            public void onResponse(Call<MoreTaskCoinModel> call, Response<MoreTaskCoinModel> response) {
                dismissProgressDialog();
                if (response!=null) {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            attendanceAllowed = sharedConstant.setSharedPreferenceBoolean(mActivity, "Attendence", response.body().getAttendence());
                        }

                    }

                }
                else {
                    Toast.makeText(mActivity, "System Message: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoreTaskCoinModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onFailure: "+t );
            }
        });


    }

    public void playRewarded(View view) {
        Log.e(TAG, "adDisplayed: hello1");
        // Check to see if a rewarded video is available.
        if (myIncent.isAdReadyToDisplay()) {
            Log.e(TAG, "adDisplayed: hello2");
            // A rewarded video is available.  Call the show method with the listeners you want to use.
            // We will use the display listener to preload the next rewarded video when this one finishes.
            myIncent.show(mActivity, null, null, new AppLovinAdDisplayListener() {
                @Override
                public void adDisplayed(AppLovinAd appLovinAd) {
                    Log.e(TAG, "adDisplayed: hello3");
                    // A rewarded video is being displayed.
                }

                @Override
                public void adHidden(AppLovinAd appLovinAd) {
                    // A rewarded video was closed.  Preload the next video now.  We won't use a load listener.
                    myIncent.preload(null);
                    getMoreTaskCoin();
                    showRewardDialog();
                }
            });
        } else {
            // No ad is currently available.  Perform failover logic...
        }

    }


    private void dismissProgressDialog() {
        avLoadingIndicatorView.hide();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        dismissProgressDialog();
        super.onPause();
    }


    public void showRewardDialog() {
        if(mActivity != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setTitle("Attendance Reward");
            builder.setMessage("Your Attendance Rewards Coins" + " " + sharedConstant.getSharedPreferenceString(mActivity, "AttendenceCoin", "") + " " + "Have been Successfully Added To Your Balance.Please Comeback Tomorrow To Get More Coins Of Attendance.");
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.AttendanceButtonLayout:
                if (attendanceAllowed){
                    playRewarded(v); }
                else {
                    Toast.makeText(mActivity, "You have to wait "+waitTime+", Please comeback after such hours.", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    @Override
    public void onAttach(@NonNull Context context ) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            mActivity = (FragmentActivity) context;
        }
    }
}
