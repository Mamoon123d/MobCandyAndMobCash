package com.cashhub.android.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashhub.android.Models.BestOfferDetailsModel;
import com.cashhub.android.Models.CouponDealData;
import com.cashhub.android.Models.OfferClickedModelShopping;
import com.cashhub.android.Models.OfferDetails2;
import com.cashhub.android.Models.RecentUser;
import com.cashhub.android.Adapter.RecentUserAdapter;
import com.cashhub.android.Interface.GetApiData;
import com.cashhub.android.R;
import com.cashhub.android.Services.RetrofitClient;
import com.cashhub.android.Services.sharedConstant;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffersDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    TextView text_copy_code, mItemDescription, tv_offer_name, tv_cat_name, tv_offer_mini_desc, tv_offer_cashback,
            tv_copuon_code, tv_username, tv_date, tv_work, tv_coupon, tv_goto_store,tv_radio_label;
    ImageView  iv_offer_item, iv_user_profile;
    AppCompatRadioButton radio_upto_cashback, radio_no_cashback;
    ProgressDialog progressDialog;
    LinearLayout ll_mobile_web, ll_mobile_app;
    String tv_coupon_deal_value;
    String offerid, TAG = "testing_offer_details";
    RecyclerView recyclerview;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("OfferDetails");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        init();

            Intent intent = getIntent();
            if (intent != null) {
                offerid = String.valueOf(intent.getIntExtra("offerId",0));
                getOfferDetails(offerid);
            }

    }



    private void init() {
        avLoadingIndicatorView = findViewById(R.id.loader_indicator);
        text_copy_code = findViewById(R.id.tv_code_copy);
        ll_mobile_app = findViewById(R.id.ll_mobile_app);
        ll_mobile_web = findViewById(R.id.ll_mobile_web);
        mItemDescription = findViewById(R.id.mItemDescription);
        tv_offer_name = findViewById(R.id.tv_offer_name);
        tv_cat_name = findViewById(R.id.tv_cat_name);
        tv_offer_mini_desc = findViewById(R.id.tv_offer_mini_desc);
        tv_offer_cashback = findViewById(R.id.tv_offer_cashback);
        tv_copuon_code = findViewById(R.id.tv_copuon_code);
        tv_username = findViewById(R.id.tv_username);
        iv_offer_item = findViewById(R.id.iv_offer_item);
        tv_date = findViewById(R.id.tv_date);
        tv_work = findViewById(R.id.tv_work);
        tv_coupon = findViewById(R.id.tv_coupon);
        tv_goto_store = findViewById(R.id.tv_goto_store);
        iv_user_profile = findViewById(R.id.iv_user_profile);

        radio_upto_cashback = findViewById(R.id.radio_upto_cashback);
        radio_no_cashback = findViewById(R.id.radio_no_cashback);
        tv_radio_label = findViewById(R.id.tv_radio_label);
        recyclerview = findViewById(R.id.recyclerview);
        text_copy_code.setOnClickListener(this);
        tv_goto_store.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code_copy:
                CopyReferCodeToClipboard(tv_coupon_deal_value);
                break;

            case R.id.tv_goto_store:
                gotoStore(offerid);
                break;


        }
    }

    private void getOfferDetails(final String offerid) {
      avLoadingIndicatorView.show();
        GetApiData service = RetrofitClient.getRetrofitInstance2().create(GetApiData.class);
        Call<BestOfferDetailsModel> call = service.viewofferDetails(sharedConstant.getSharedPreferenceInt(OffersDetailsActivity.this, "userId", 0),
                sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this, "securitytoken", ""),
                sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(OffersDetailsActivity.this, "versionCode", 0),
                offerid,sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this,"userFrom",""));

//        if (!((Activity) OffersDetailsActivity.this).isFinishing()) {
//            progressDialog = new ProgressDialog(OffersDetailsActivity.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }

        call.enqueue(new Callback<BestOfferDetailsModel>() {
            @Override
            public void onResponse(Call<BestOfferDetailsModel> call, Response<BestOfferDetailsModel> response) {
                dismissProgressDialog();
                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {

                            OfferDetails2 offerdetails = response.body().getOfferDetails();
                            tv_offer_name.setText(offerdetails.getOfferName());
                            tv_offer_cashback.setText(offerdetails.getCashBack());
                            tv_offer_mini_desc.setText(offerdetails.getShortDescription());
                            mItemDescription.setText(offerdetails.getLongDescription());
                            tv_cat_name.setText(offerdetails.getCategory());
                            tv_radio_label.setText(offerdetails.getCashBack());


                            Picasso.get().load(offerdetails.getImageUrl())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into((iv_offer_item));



                            ArrayList<RecentUser> recentUserArrayList = offerdetails.getRecentUser();

                            if (recentUserArrayList.size() > 0)
                            {

                                RecentUserAdapter recentUserAdapter = new RecentUserAdapter(recentUserArrayList, OffersDetailsActivity.this);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OffersDetailsActivity.this);
                                recyclerview.setLayoutManager(mLayoutManager);
                                recyclerview.setAdapter(recentUserAdapter);
                                recyclerview.setNestedScrollingEnabled(false);

                                CouponDealData couponDealData = offerdetails.getCouponDealData();
                                tv_coupon.setText(couponDealData.getShowText());
                                tv_coupon_deal_value = couponDealData.getValue();
                                tv_copuon_code.setText(couponDealData.getValue());
                            }


                        } else {
                            Toast.makeText(OffersDetailsActivity.this, getString(R.string.systemmessage) + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    assert response != null;
                    Toast.makeText(OffersDetailsActivity.this, getString(R.string.systemmessage) + response.errorBody(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<BestOfferDetailsModel> call, Throwable t) {
                // Log error here since request failed
                Log.e("response", t.toString());
            }
        });


    }

    private void gotoStore(String offerid) {
        avLoadingIndicatorView.show();
        GetApiData service = RetrofitClient.getRetrofitInstance2().create(GetApiData.class);
        Call<OfferClickedModelShopping> call = service.offerclicked(sharedConstant.getSharedPreferenceInt(OffersDetailsActivity.this, "userId", 0),
                sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this, "securitytoken", ""),
                sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(OffersDetailsActivity.this, "versionCode", 0),
                offerid,sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this,"userFrom",""),
                sharedConstant.getSharedPreferenceString(OffersDetailsActivity.this,"adverId",""));

//        if (!((Activity) OffersDetailsActivity.this).isFinishing()) {
//            progressDialog = new ProgressDialog(OffersDetailsActivity.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }

        call.enqueue(new Callback<OfferClickedModelShopping>() {
            @Override
            public void onResponse(Call<OfferClickedModelShopping> call, Response<OfferClickedModelShopping> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        startWebview(response.body().getActionUrl());

                    } else {
                        Toast.makeText(OffersDetailsActivity.this, getString(R.string.systemmessage) + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onFailure(Call<OfferClickedModelShopping> call, Throwable t) {
                dismissProgressDialog();
                // Log error here since request failed
                Log.e("response", t.toString());
            }
        });
    }

    private void startWebview(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @SuppressLint("ObsoleteSdkInt")
    private void CopyReferCodeToClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) OffersDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;
            clipboard.setText(text);
            Toast.makeText(OffersDetailsActivity.this, "Coupon code copied Successfully.", Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) OffersDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(OffersDetailsActivity.this, "Coupon code copied Successfully.", Toast.LENGTH_SHORT).show();
        }
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

}
