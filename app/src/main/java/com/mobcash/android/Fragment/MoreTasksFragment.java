package com.mobcash.android.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.mobcash.android.Activities.InviteFriends;
import com.mobcash.android.Activities.ShopingActivity;
import com.mobcash.android.Activities.UserProfileActivity;
import com.mobcash.android.Adapter.MoreTaskListAdapter;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.Models.MoreTask;
import com.mobcash.android.Models.MoreTaskCoinModel;
import com.mobcash.android.Models.MoreTaskListModel;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.out.MTGRewardVideoHandler;
import com.mintegral.msdk.out.MtgWallHandler;
import com.mobcash.android.R;
import com.pollfish.main.PollFish;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreTasksFragment extends Fragment implements View.OnClickListener{

    protected FragmentActivity mActivity;

    String TAG = "testing";
    ProgressDialog progressDialog;
    RecyclerView recylerStarter;
    MoreTaskListAdapter moreTaskListAdapter;
    ArrayList<MoreTask> moreTaskArrayList;

    //    app lovin
    private AppLovinIncentivizedInterstitial myIncent;


    LinearLayout fantasticOfferWall, pollfish, superOfferWall, watchVideos, reviewTask, completeProfileTask, facebookTask, inviteFriends;

    Boolean flag = true;

    //mintergral sdk
    MTGRewardVideoHandler mMTGRewardVideoHandler;
    private String mAppWallUnitId = "187148";
    MtgWallHandler mtgHandler;
    private String mRewardUnitId = "187042";
    private String mRewardId = "1";
    AVLoadingIndicatorView avLoadingIndicatorView;


    public MoreTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_tasks, container, false);

        fantasticOfferWall = view.findViewById(R.id.fantasticOfferWall);
        pollfish = view.findViewById(R.id.pollfish_survey);
        superOfferWall = view.findViewById(R.id.superOfferWall);
        watchVideos = view.findViewById(R.id.watchVideos);
        reviewTask = view.findViewById(R.id.reviewTask);
        completeProfileTask = view.findViewById(R.id.completeProfileTask);
        facebookTask = view.findViewById(R.id.facebookTask);
        inviteFriends = view.findViewById(R.id.inviteFriends);
        recylerStarter = view.findViewById(R.id.recylerStarter);
        avLoadingIndicatorView = view.findViewById(R.id.loader_indicator);
        fantasticOfferWall.setOnClickListener(this);
        pollfish.setOnClickListener(this);
        superOfferWall.setOnClickListener(this);
        watchVideos.setOnClickListener(this);
        reviewTask.setOnClickListener(this);
        completeProfileTask.setOnClickListener(this);
        facebookTask.setOnClickListener(this);
        inviteFriends.setOnClickListener(this);



        return view;
    }



    private void getMoreTaskList() {
        avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<MoreTaskListModel> call = getApiData.getMoreTaskListData(sharedConstant.getSharedPreferenceInt(mActivity, "userId", 0)
                , sharedConstant.getSharedPreferenceString(mActivity, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(mActivity, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(mActivity, "versionCode", 0));


        call.enqueue(new Callback<MoreTaskListModel>() {
            @Override
            public void onResponse(Call<MoreTaskListModel> call, Response<MoreTaskListModel> response) {
                   dismissProgressDialog();
                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            moreTaskArrayList = response.body().getMoreTasks();

                            sharedConstant.setSharedPreferenceString(mActivity, "ReviewPackageName", response.body().getReviewTask());
                            sharedConstant.setSharedPreferenceInt(mActivity, "WatchVideoCoin", response.body().getWatchCoin());
                            moreTaskListAdapter = new MoreTaskListAdapter(moreTaskArrayList, mActivity);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                            recylerStarter.setLayoutManager(mLayoutManager);
                            recylerStarter.setItemAnimator(new DefaultItemAnimator());
                            recylerStarter.setAdapter(moreTaskListAdapter);


                        } else {
                            Toast.makeText(mActivity, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(mActivity, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoreTaskListModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onFailure: "+t );
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fantasticOfferWall:
                startWall();
                break;

            case R.id.pollfish_survey:
                PollFish.show();
                break;

            case R.id.superOfferWall:
                Intent intent = new Intent(mActivity, ShopingActivity.class);
                startActivity(intent);
                break;

            case R.id.watchVideos:
                if (mMTGRewardVideoHandler.isReady() && flag.booleanValue() == true) {
                    flag = false;
                    mMTGRewardVideoHandler.show(mRewardId);
                    Log.e(TAG, "onClick:Hello +");

                } else {
                    playRewarded(view);
                    flag = true;
                    Log.e(TAG, "onClick:applovin " + flag);
                }
                break;


            case R.id.completeProfileTask:
                Intent intentProfile= new Intent(mActivity, UserProfileActivity.class);
                startActivity(intentProfile);
                break;


            case R.id.inviteFriends:
                Intent intent7 = new Intent(mActivity, InviteFriends.class);
                startActivity(intent7);
                break;


        }
    }


    private void startWall() {
        Map<String, Object> properties = MtgWallHandler.getWallProperties(mAppWallUnitId);
        mtgHandler = new MtgWallHandler(properties, mActivity);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.color.purewhite);
        // user bitmap resId as the logo
        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO, bitmap);
        // user drawable resId as the logo
        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO_ID, R.color.purewhite);

        // use bitmap or text as the appwall title
        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO_TEXT, "Fantastic OfferWall");
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO_TEXT, bitmap);
        // use color resId as the appwall title
        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_BACKGROUND_COLOR, R.color.colorPrimaryDark);
        // use drawable resid as the appwall title
        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_BACKGROUND_ID, R.color.colorPrimaryDark);

        // wall main background must be color
        properties.put(MIntegralConstans.PROPERTIES_WALL_MAIN_BACKGROUND_ID, R.color.mintegral_bg_main);

        // wall tab background must be in color
        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_BACKGROUND_ID, R.color.mintegral_bg_main);

        // wall tab indicator line must be in color
        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_INDICATE_LINE_BACKGROUND_ID,
                R.color.mintegral_wall_tab_line);

        // wall button color must be drawable
        properties.put(MIntegralConstans.PROPERTIES_WALL_BUTTON_BACKGROUND_ID, R.drawable.mintegral_wall_shape_btn);

        // wall loading view
        properties.put(MIntegralConstans.PROPERTIES_WALL_LOAD_ID, R.layout.mintegral_wall_click_loading);

        properties.put(MIntegralConstans.PROPERTIES_WALL_STATUS_COLOR, R.color.colorPrimaryDark);

        properties.put(MIntegralConstans.PROPERTIES_WALL_NAVIGATION_COLOR, R.color.mintegral_transparent);

        // set the wall tab color of selected and unselected text by hex color
        // codes
        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_SELECTED_TEXT_COLOR, "#ff7900");
        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_UNSELECTED_TEXT_COLOR, "#ffaa00");
        mtgHandler.startWall();

    }


    public interface RewardVideoListener {

        void onVideoLoadSuccess(String unitId);

        void onLoadSuccess(String unitId);

        void onVideoLoadFail(String errorMsg);

        void onAdShow();

        void onAdClose(boolean isCompleteView, String rewardName, float rewardAmout);

        void onShowFail(String errorMsg);

        void onVideoAdClicked(String unitId);

        void onVideoComplete(String unitId);

        void onEndcardShow(String unitId);

    }

    private void loadAdd() {
        mMTGRewardVideoHandler = new MTGRewardVideoHandler(mActivity, mRewardUnitId);
        mMTGRewardVideoHandler.setRewardVideoListener(new com.mintegral.msdk.out.RewardVideoListener() {
            @Override
            public void onVideoLoadSuccess(String s) {
                Log.e(TAG, "onLoadSuccess:" + Thread.currentThread());

            }

            @Override
            public void onLoadSuccess(String s) {
                Log.e(TAG, "onVideoLoadSuccess:" + Thread.currentThread());
            }

            @Override
            public void onVideoLoadFail(String s) {
                Log.e(TAG, "onVideoLoadFail errorMsg:" + s);
            }

            @Override
            public void onAdShow() {
                Log.e(TAG, "onAdShow");
            }

            @Override
            public void onAdClose(boolean isCompleteView, String RewardName, float RewardAmout) {
                Log.e(TAG, "onAdClose rewardinfo :" + "RewardName:" + RewardName + "RewardAmout:" + RewardAmout + " isCompleteView：" + isCompleteView);
                if (isCompleteView) {
                    showRewardDialog();
                    getMoreTaskCoin();
                } else {
                    Log.e(TAG, "onAdClose:failed to load ad ");
                }
            }

            @Override
            public void onShowFail(String s) {
                Log.e(TAG, "onVideoAdClicked");
            }

            @Override
            public void onVideoAdClicked(String s) {
                Log.e(TAG, "onVideoComplete");
            }

            @Override
            public void onVideoComplete(String s) {
                Log.e(TAG, "onVideoComplete");
            }

            @Override
            public void onEndcardShow(String s) {
                Log.e(TAG, "onEndcardShow");
            }
        });

        mMTGRewardVideoHandler.load();
    }


    // Play a rewarded video in correspondence to a button click
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


    private void getMoreTaskCoin() {
        avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<MoreTaskCoinModel> call = getApiData.getMoreTaskCoin(sharedConstant.getSharedPreferenceInt(mActivity, "userId", 0)
                , sharedConstant.getSharedPreferenceString(mActivity, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(mActivity, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(mActivity, "versionCode", 0), sharedConstant.getSharedPreferenceInt(mActivity, "WatchVideoId", 0));

//        if (!mActivity.isFinishing()) {
//            progressDialog = new ProgressDialog(mActivity);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }

        call.enqueue(new Callback<MoreTaskCoinModel>() {
            @Override
            public void onResponse(Call<MoreTaskCoinModel> call, Response<MoreTaskCoinModel> response) {
                dismissProgressDialog();

                if (response != null) {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {

                        }

                    }
                } else {
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


    @Override
    public void onStart() {
        super.onStart();
        AppLovinSdk.initializeSdk(mActivity);
        // Create and preload a rewarded video soon after initialization.
        myIncent = AppLovinIncentivizedInterstitial.create(mActivity);
        myIncent.preload(null);
        loadAdd();
        getMoreTaskList();
    }

    @Override
    public void onAttach(@NonNull Context context ) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            mActivity = (FragmentActivity) context;
        }
    }

    public void onResume() {
        super.onResume();
        PollFish.ParamsBuilder paramsBuilder = new PollFish.ParamsBuilder("4606912c-a7d2-4a5c-9535-36c830838bb1")
                .rewardMode(true)
                .requestUUID(String.valueOf(sharedConstant.getSharedPreferenceInt(mActivity, "userId", 0)))
                .releaseMode(true)
                .build();
        PollFish.initWith(mActivity, paramsBuilder);
    }

    public void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();

    }

    private void dismissProgressDialog() {
        avLoadingIndicatorView.hide();
    }


    private void showRewardDialog() {

        if (mActivity != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setTitle("Watch Video Reward");
            builder.setMessage(getResources().getString(R.string.rewarded_dialog_message) + " " + sharedConstant.getSharedPreferenceInt(mActivity, "WatchVideoCoin", 0) + " coins " + "of watched video");
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }


}


