package com.mobcash.android.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mobcash.android.Activities.Transactions;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.Models.PayOutDataModel;
import com.mobcash.android.Models.PayoutArray;
import com.mobcash.android.Models.PayoutHistory;
import com.mobcash.android.Services.sharedConstant;
import com.mobcash.android.Adapter.ViewPagerAdapter;
import com.mobcash.android.R;
import com.mobcash.android.Services.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayoutFragment extends Fragment implements View.OnClickListener {

    protected FragmentActivity mActivity;
     TabLayout tabLayout;
     ViewPager viewPager;
     CardView balance,todayCard,weeklyCard;
     TextView Amount,TodayCoins,thisWeekCoins,Coins;
    ProgressDialog progressDialog;
    String TAG = "testing";
     ArrayList<String> PayoutValuesList;
     ArrayList<PayoutArray> PayoutArrayList;
     ArrayList<PayoutHistory> payoutHistoryArrayList;
    AVLoadingIndicatorView avLoadingIndicatorView;

    public PayoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payout, container, false);


        tabLayout=view.findViewById(R.id.tabs);
        viewPager=view.findViewById(R.id.viewpager);
        balance = view.findViewById(R.id.balance);
        Amount = view.findViewById(R.id.totalAmount);
        todayCard = view.findViewById(R.id.todayCard);
        weeklyCard = view.findViewById(R.id.thisWeekCard);
        Coins = view.findViewById(R.id.totalCoins);
        TodayCoins = view.findViewById(R.id.TodayCoins);
        thisWeekCoins = view.findViewById(R.id.thisWeekCoins);
        avLoadingIndicatorView = view.findViewById(R.id.loader_indicator);
        balance.setOnClickListener(this);
        todayCard.setOnClickListener(this);
        weeklyCard.setOnClickListener(this);


        getPayoutData();

        return view;
    }

    private void getPayoutData() {
       avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<PayOutDataModel> call = getApiData.getPayoutData(sharedConstant.getSharedPreferenceInt(mActivity, "userId", 0)
                , sharedConstant.getSharedPreferenceString(mActivity, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(mActivity, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(mActivity, "versionCode", 0));


        call.enqueue(new Callback<PayOutDataModel>() {

            @Override
            public void onResponse(Call<PayOutDataModel> call, Response<PayOutDataModel> response) {
              dismissProgressDialog();
                try {
                    if (response!=null) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {

                            Coins.setText("" + response.body().getUserCoin());
                            Amount.setText(response.body().getCurrency() + " " + response.body().getUserAmount());

                            sharedConstant.setSharedPreferenceString(mActivity, "Coins", String.valueOf(response.body().getUserCoin()));
                            sharedConstant.setSharedPreferenceString(mActivity, "Amount", String.valueOf(response.body().getUserAmount()));

                            TodayCoins.setText("" + response.body().getTodayCoin());
                            thisWeekCoins.setText("" + response.body().getWeekCoin());
                            sharedConstant.setSharedPreferenceString(mActivity, "transText", response.body().getTransText());

                            PayoutValuesList = response.body().getPayoutValues();
                            PayoutArrayList = response.body().getPayoutArray();
                            payoutHistoryArrayList = response.body().getPayoutHistory();
                            boolean Arrayflg = response.body().getArrFlag();
                            Log.e(TAG, "onResponse: "+payoutHistoryArrayList.size() );


                            ViewPagerAdapter adapter = new ViewPagerAdapter(mActivity.getSupportFragmentManager());

                            adapter.addFragment(new CashoutFragment(PayoutValuesList, PayoutArrayList), "Payout");
                            adapter.addFragment(new HistoryFragment(payoutHistoryArrayList,Arrayflg), "History");
                            viewPager.setAdapter(adapter);
                            tabLayout.setSelectedTabIndicatorHeight(0);
                            tabLayout.setupWithViewPager(viewPager);
                            setTabBG(R.drawable.tab_left_select, R.drawable.tab_right_unselect);
                            tabLayout.setTabTextColors(Color.parseColor("#4F6397"), Color.parseColor("#ffffff"));
                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    if (tabLayout.getSelectedTabPosition() == 0) {
                                        setTabBG(R.drawable.tab_left_select, R.drawable.tab_right_unselect);
                                        Log.e(TAG, "onTabSelected: " + "payouttab");
                                    } else {
                                        setTabBG(R.drawable.tab_background_unselected, R.drawable.tab_right_select);
                                    }
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                    if (tabLayout.getSelectedTabPosition() == 0) {
                                        setTabBG(R.drawable.tab_left_select, R.drawable.tab_right_unselect);
                                    }
                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {
                                    setTabBG(R.drawable.tab_background_unselected, R.drawable.tab_right_select);
                                }

                            });

                        } else
                            Toast.makeText(mActivity, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mActivity, "System Message: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse:Exp: "+e);
                   // Toast.makeText(mActivity, "No resources found", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<PayOutDataModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onResponse:Exp: "+t);
                //Toast.makeText(mActivity, "No resources found: "+t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void dismissProgressDialog() {
       avLoadingIndicatorView.hide();
    }

    private void setTabBG(int tab1, int tab2){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewGroup tabStrip = (ViewGroup) tabLayout.getChildAt(0);
            View tabView1 = tabStrip.getChildAt(0);
            View tabView2 = tabStrip.getChildAt(1);
            if (tabView1 != null) {
                int paddingStart = tabView1.getPaddingStart();
                int paddingTop = tabView1.getPaddingTop();
                int paddingEnd = tabView1.getPaddingEnd();
                int paddingBottom = tabView1.getPaddingBottom();
                ViewCompat.setBackground(tabView1, AppCompatResources.getDrawable(tabView1.getContext(), tab1));
                ViewCompat.setPaddingRelative(tabView1, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }

            if (tabView2 != null) {
                int paddingStart = tabView2.getPaddingStart();
                int paddingTop = tabView2.getPaddingTop();
                int paddingEnd = tabView2.getPaddingEnd();
                int paddingBottom = tabView2.getPaddingBottom();
                ViewCompat.setBackground(tabView2, AppCompatResources.getDrawable(tabView2.getContext(), tab2));
                ViewCompat.setPaddingRelative(tabView2, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.balance:
                Intent intent = new Intent(mActivity, Transactions.class);
                startActivity(intent);
                break;

            case R.id.todayCard:
                Intent intent1 = new Intent(mActivity, Transactions.class);
                startActivity(intent1);
                break;

            case R.id.thisWeekCard:
                Intent intent2 = new Intent(mActivity, Transactions.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissProgressDialog();

    }

    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();

    }
    @Override
    public void onAttach(@NonNull Context context ) {
        super.onAttach(context);
        if (context instanceof FragmentActivity){
            mActivity = (FragmentActivity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
