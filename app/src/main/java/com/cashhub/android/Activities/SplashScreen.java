package com.cashhub.android.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashhub.android.BuildConfig;
import com.cashhub.android.Interface.GetApiData;
import com.cashhub.android.Models.AppOpenModel;
import com.cashhub.android.R;
import com.cashhub.android.Services.RetrofitClient;
import com.cashhub.android.Services.sharedConstant;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    String TAG="testing";
    FirebaseAuth mAuth;
    String packageName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_splash_screen);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.appCompatTextView4);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        imageView.startAnimation(myanim);
        textView.startAnimation(myanim);
        mAuth = FirebaseAuth.getInstance();

        if(isNetworkAvailable(SplashScreen.this)){
            if (sharedConstant.getSharedPreferenceInt(SplashScreen.this, "userId", 0)!=0 &&
                    !sharedConstant.getSharedPreferenceString(SplashScreen.this, "securitytoken", "").equals("null")) {
                if (getIntent().getExtras() != null) {
                    String title = null;
//                    String actionk = null;
                    for (String key : getIntent().getExtras().keySet()) {
                        if (key.equals("OfferKey")) {
//                            actionk = "True";
                            title = getIntent().getExtras().getString(key);
                        }
                    }
                    if(title!=null && !title.equals("null")){
                        Intent intentProductdDtails = new Intent(SplashScreen.this, OfferDetailsActivityNotification.class);
                        intentProductdDtails.putExtra("offerId", Integer.parseInt(title));
                        startActivityForResult(intentProductdDtails,105);
                    }

                    else{
                        openApp();
                    }
                }
                else{
                    openApp();
                }

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, GoogleLogin.class);
                        startActivity(intent);
                        finish();

                    }
                },3000);

            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setTitle("Alert!");
            builder.setMessage("Please Check Your Internet Connection.");
            builder.setPositiveButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            builder.setCancelable(false);
            builder.show();
        }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: Splash"+e );
        }
    }

    public void openApp(){
        int versionCode = BuildConfig.VERSION_CODE;

        final String versionName = BuildConfig.VERSION_NAME;
        sharedConstant.setSharedPreferenceString(SplashScreen.this, "version", versionName);
        GetApiData service = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<AppOpenModel> call = service.getAppOpenDetails(sharedConstant.getSharedPreferenceInt(SplashScreen.this,"userId",0),
                sharedConstant.getSharedPreferenceString(SplashScreen.this,"securitytoken",""),versionName,versionCode);

//        if(!((Activity) SplashScreen.this).isFinishing())
//        {
//            progressDialog = new ProgressDialog(SplashScreen.this);
//            progressDialog.setMessage(getString(R.string.loadingwait));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//        }


        call.enqueue(new Callback<AppOpenModel>() {
            @Override
            public void onResponse(Call<AppOpenModel> call, Response<AppOpenModel> response) {


                if (response!= null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        packageName = response.body().getPackAge();
                        if (response.body().getForceUpdate()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                            builder.setMessage("Your " + getString(R.string.app_name) + " seems very old, Please update to get new Earning features!!");
                            builder.setPositiveButton("UPDATE NOW",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();

                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                                            } catch (ActivityNotFoundException e) {
                                                // TODO: handle exception
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                                            }
                                            finish();
                                        }
                                    });
                            builder.setCancelable(false);
                            builder.show();

                        } else {
                            if (response.body().getStatus() == 200) {
                                String amount = String.valueOf(response.body().getUserAmount());
                                String coins = String.valueOf(response.body().getUserCoin());
                                String currency = String.valueOf(response.body().getCurrency());


                                sharedConstant.setSharedPreferenceString(SplashScreen.this, "Coins", coins);
                                sharedConstant.setSharedPreferenceString(SplashScreen.this, "Amount", amount);
                                sharedConstant.setSharedPreferenceString(SplashScreen.this, "Currency", currency);
                                sharedConstant.setSharedPreferenceInt(SplashScreen.this, "PaycoinLimit", response.body().getPaycoinLimit());
                                sharedConstant.setSharedPreferenceInt(SplashScreen.this, "AttendenceId", response.body().getAttendenceId());
                                sharedConstant.setSharedPreferenceInt(SplashScreen.this, "WatchVideoId", response.body().getWatchVideoId());
                                sharedConstant.setSharedPreferenceBoolean(SplashScreen.this, "Attendence", response.body().getAttendence());
                                sharedConstant.setSharedPreferenceString(SplashScreen.this, "WaitTime", response.body().getWaitTime());
                                sharedConstant.setSharedPreferenceString(SplashScreen.this, "AttendenceCoin", String.valueOf(response.body().getAttendenceCoin()));

                                /*if( sharedConstant.getSharedPreferenceInt(SplashScreen.this,"flag",0) != 1){
                                    Intent intent = new Intent(SplashScreen.this, UserProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }*/
                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SplashScreen.this, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
                else {
                    Toast.makeText(SplashScreen.this, "System Message: "+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<AppOpenModel> call, Throwable t) {
                Toast.makeText(SplashScreen.this, getString(R.string.systemmessage) +t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==105){
//            extraBuNotification = null;
            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
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

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    }

