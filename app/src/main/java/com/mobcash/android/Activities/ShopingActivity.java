package com.mobcash.android.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobcash.android.Models.AllOffersDataModel;
import com.mobcash.android.Models.BestOfferDatum;
import com.mobcash.android.Adapter.TodayBestOfferListAdapter;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.RecyclerTouchListener;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopingActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recycler_view_best_offers;
    private TodayBestOfferListAdapter todayBestAdapter;
    ProgressDialog progressDialog;
    String TAG = "testing_ShopingActivity";
    String userFrom = "MobCandy";
    AVLoadingIndicatorView avLoadingIndicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ShoppingWall");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        recycler_view_best_offers = findViewById(R.id.recycler_view_best_offers);
        avLoadingIndicatorView = findViewById(R.id.loader_indicator);
        getAllOffersData();
    }

           private void getAllOffersData() {
        sharedConstant.setSharedPreferenceString(ShopingActivity.this, "userFrom", userFrom);
        avLoadingIndicatorView.show();
        GetApiData service = RetrofitClient.getRetrofitInstance2().create(GetApiData.class);
        Call<AllOffersDataModel> call = service.getOfferlist(sharedConstant.getSharedPreferenceInt(ShopingActivity.this, "userId", 0),
                sharedConstant.getSharedPreferenceString(ShopingActivity.this, "securitytoken", ""),
                sharedConstant.getSharedPreferenceString(ShopingActivity.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(ShopingActivity.this, "versionCode", 0),
                sharedConstant.getSharedPreferenceString(ShopingActivity.this, "userFrom", "")
                );

//        if (!((Activity) ShopingActivity.this).isFinishing()) {
//            progressDialog = new ProgressDialog(ShopingActivity.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }

        call.enqueue(new Callback<AllOffersDataModel>() {
            @Override
            public void onResponse(Call<AllOffersDataModel> call, Response<AllOffersDataModel> response) {
                dismissProgressDialog();
                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
;

                            final ArrayList<BestOfferDatum> bestOfferDatalist = response.body().getBestOfferData();
                            todayBestAdapter = new TodayBestOfferListAdapter(bestOfferDatalist, ShopingActivity.this);
                            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(ShopingActivity.this);
                            recycler_view_best_offers.setLayoutManager(mLayoutManager1);
                            recycler_view_best_offers.setAdapter(todayBestAdapter);
                            recycler_view_best_offers.setNestedScrollingEnabled(false);
                            recycler_view_best_offers.addOnItemTouchListener(new RecyclerTouchListener(ShopingActivity.this, recycler_view_best_offers, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    BestOfferDatum bestOfferDatum = bestOfferDatalist.get(position);
                                    // Toast.makeText(mActivity, topStores.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ShopingActivity.this, OffersDetailsActivity.class);
                                    intent.putExtra("offerId", bestOfferDatum.getOfferId());
                                     //Log.e(TAG, "onClick:bestOfferDatum.getOfferId() "+bestOfferDatum.getOfferId() );
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));

                        } else {
                            Toast.makeText(ShopingActivity.this, getString(R.string.systemmessage) + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(ShopingActivity.this, getString(R.string.systemmessage) + response.errorBody(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<AllOffersDataModel> call, Throwable t) {
                dismissProgressDialog();
                // Log error here since request failed
                Log.e("response", t.toString());
            }
        });


    }

    private void dismissProgressDialog() {
       avLoadingIndicatorView.hide();
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

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
