package com.mobcash.android.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobcash.android.Models.OfferClickedModel;
import com.mobcash.android.Models.OfferDetails;
import com.mobcash.android.Models.OfferDetailsModel;
import com.mobcash.android.Models.PayoutStep;
import com.mobcash.android.Adapter.OfferInstuctionAdapter;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetailsActivityNotification extends AppCompatActivity implements View.OnClickListener {

    private String offer_id;
    TextView appTitle,Decription,offerAmount,offer_decription,descriptionloc;
    ImageView appImage;
    private ProgressDialog progressDialog;
    String TAG = "testing";
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    ArrayList<PayoutStep> instructionarraylist;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_offer_details_notification);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Offer Details");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            avLoadingIndicatorView = findViewById(R.id.loader_indicator);
            appTitle = findViewById(R.id.appTitle);
            Decription = findViewById(R.id.descriptions);
            offerAmount = findViewById(R.id.offerAmount);
            appImage = findViewById(R.id.appImage);
            offer_decription = findViewById(R.id.description_offerStep);
            descriptionloc = findViewById(R.id.description_loc);
            linearLayout = findViewById(R.id.startoffernow);
            recyclerView = findViewById(R.id.recyler_view);
            linearLayout.setOnClickListener(this);

            Intent intent = getIntent();
            if (intent != null) {
                offer_id = String.valueOf(intent.getIntExtra("offerId", 0));
                getOffersDetails(offer_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void startThisOfferNow(String offer_id)
    {
        avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<OfferClickedModel> call = getApiData.getOfferClickedDetails(sharedConstant.getSharedPreferenceInt(OfferDetailsActivityNotification.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(OfferDetailsActivityNotification.this, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(OfferDetailsActivityNotification.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(OfferDetailsActivityNotification.this, "versionCode", 0),offer_id);

//        if(!((Activity) OfferDetailsActivityNotification.this).isFinishing()) {
//            progressDialog = new ProgressDialog(OfferDetailsActivityNotification.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }
        call.enqueue(new Callback<OfferClickedModel>() {
            @Override
            public void onResponse(Call<OfferClickedModel> call, Response<OfferClickedModel> response) {
                dismissProgressDialog();

                if (response != null) {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            String actionUrl = response.body().getActionUrl();
                            openWebPage(actionUrl);
                        } else {
                            Toast.makeText(OfferDetailsActivityNotification.this, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OfferDetailsActivityNotification.this, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(OfferDetailsActivityNotification.this, "System Message: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OfferClickedModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onResponse:Exp: " + t);
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.startoffernow:
                startThisOfferNow(offer_id);


        }



    }

    public void openWebPage(String url) {

        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void dismissProgressDialog() {
        avLoadingIndicatorView.hide();
    }

    private void getOffersDetails(String offer_id) {
        avLoadingIndicatorView.show();
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<OfferDetailsModel> call = getApiData.getOfferDetails(sharedConstant.getSharedPreferenceInt(OfferDetailsActivityNotification.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(OfferDetailsActivityNotification.this, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(OfferDetailsActivityNotification.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(OfferDetailsActivityNotification.this, "versionCode", 0),offer_id);

//        if(!OfferDetailsActivityNotification.this.isFinishing()) {
//            progressDialog = new ProgressDialog(OfferDetailsActivityNotification.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }

        call.enqueue(new Callback<OfferDetailsModel>() {
            @Override
            public void onResponse(Call<OfferDetailsModel> call, Response<OfferDetailsModel> response) {
                dismissProgressDialog();
                if(response!=null) {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {

                            ArrayList<OfferDetails> offerDetails = new ArrayList<>();
                            offerDetails.add(response.body().getOfferDetails());

                            String Description = null, appName = null, offeramount = null, des_loc = null ,offerSteps = null;
                            Uri image = null;

                            for (int i = 0; i < offerDetails.size(); i++) {

                                Description = offerDetails.get(i).getDescription();
                                appName = offerDetails.get(i).getOfferName();
                                offeramount = offerDetails.get(i).getOfferAmount();
                                offerSteps = offerDetails.get(i).getDetailSteps();
                                des_loc = offerDetails.get(i).getDescriptionLoc();
                                Picasso.get().load(offerDetails.get(i).getImageUrl()).into(appImage);

                            }

                            appTitle.setText(appName);
                            offerAmount.setText("₹ "+offeramount);
                            Decription.setText(Description);
                            offer_decription.setText(offerSteps);
                            descriptionloc.setText(des_loc);

                            instructionarraylist = response.body().getOfferDetails().getPayoutSteps();
                            OfferInstuctionAdapter offerInstuctionAdapter = new OfferInstuctionAdapter(instructionarraylist, OfferDetailsActivityNotification.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OfferDetailsActivityNotification.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(offerInstuctionAdapter);

                        }
                    }
                }
                else {
                    Toast.makeText(OfferDetailsActivityNotification.this, "Response:User" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OfferDetailsModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onResponse:Exp: "+t);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

}
