package com.mobcash.android.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobcash.android.Models.InviteDataModel;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteFriends extends AppCompatActivity implements View.OnClickListener {
    TextView inviteText,referCodeTV;
    private ProgressDialog progressDialog;
    private String invitefburl,inviteTextUrl,InviteText;
    String TAG = "testing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_invite_friends);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Invite Friends");
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


            inviteText = findViewById(R.id.inviteText);
            ImageView copyreferCodeTV = findViewById(R.id.imgCopy);
            LinearLayout layoutShareFB = findViewById(R.id.layout_share_fb);
            LinearLayout layoutShareWhatsapp = findViewById(R.id.layout_share_whatsapp);
            LinearLayout layoutShareTelegram = findViewById(R.id.layout_share_telegram);
            LinearLayout layoutShareMore = findViewById(R.id.layout_share_more);
            referCodeTV = findViewById(R.id.invite_user_id);

            layoutShareFB.setOnClickListener(this);
            layoutShareWhatsapp.setOnClickListener(this);
            layoutShareTelegram.setOnClickListener(this);
            layoutShareMore.setOnClickListener(this);
            copyreferCodeTV.setOnClickListener(this);
            inviteData();



        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: InviteFriends"+e );
        }

    }

    private void inviteData()
    {
        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<InviteDataModel> call = getApiData.getInviteData(sharedConstant.getSharedPreferenceInt(InviteFriends.this, "userId", 0)
                , sharedConstant.getSharedPreferenceString(InviteFriends.this, "securitytoken", "")
                , sharedConstant.getSharedPreferenceString(InviteFriends.this, "versionName", ""),
                sharedConstant.getSharedPreferenceInt(InviteFriends.this, "versionCode", 0));

        if (!((Activity) InviteFriends.this).isFinishing()) {
            progressDialog = new ProgressDialog(InviteFriends.this);
            progressDialog.setMessage(getString(R.string.loadingwait));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }
        call.enqueue(new Callback<InviteDataModel>() {
            @Override
            public void onResponse(Call<InviteDataModel> call, Response<InviteDataModel> response) {
                dismissProgressDialog();

                if (response != null) {
                    if (response.body().getStatus() == 200) {
                        invitefburl = response.body().getInviteFbUrl();
                        inviteTextUrl = response.body().getInviteTextUrl();
                        InviteText = response.body().getInviteText();
                        inviteText.setText(InviteText);
                        referCodeTV.setText(response.body().getReferralCode());
                    } else {
                        Toast.makeText(InviteFriends.this, "System Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(InviteFriends.this, "System Message: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InviteDataModel> call, Throwable t) {
                dismissProgressDialog();
                Log.e(TAG, "onResponse:Exp: "+t);
            }
        });

    }

    @SuppressLint("ObsoleteSdkInt")
    private void setClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) InviteFriends.this.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(InviteFriends.this, "Referral code copied Successfully", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) InviteFriends.this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(InviteFriends.this, "Referral code copied Successfully", Toast.LENGTH_SHORT).show();
        }
    }


    public void shareOnWhatsapp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, inviteTextUrl);
        try {
            InviteFriends.this.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InviteFriends.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareAppOther() {
        try {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            shareIntent.putExtra(Intent.EXTRA_TEXT, inviteTextUrl);
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public void shareAppFb(String invitefburl){
        try {

            final String appName = "com.facebook.katana";
            final boolean isAppInstalled = isAppAvailable(InviteFriends.this.getApplicationContext(), appName);
            if (isAppInstalled) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.setPackage(appName);
                myIntent.putExtra(Intent.EXTRA_TEXT, invitefburl);//
                InviteFriends.this.startActivity(Intent.createChooser(myIntent, "Share with"));
            } else {
                Toast.makeText(InviteFriends.this, "Facebook not Installed", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
            //e.toString();
        }
    }


    public void shareOnTelegram() {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(InviteFriends.this.getApplicationContext(), appName);
        if (isAppInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(appName);
            myIntent.putExtra(Intent.EXTRA_TEXT, inviteTextUrl);
            InviteFriends.this.startActivity(Intent.createChooser(myIntent, "Share with"));
        } else {
            Toast.makeText(InviteFriends.this, "Telegram not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgCopy) {
            if (referCodeTV.getText().toString().equals("")) {
                Toast.makeText(InviteFriends.this, "You have no Referral Code", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Referral code is", referCodeTV.getText().toString() + "");
                String refferal_code = referCodeTV.getText().toString();
                setClipboard(refferal_code);
            }
        } else if (id == R.id.layout_share_fb) {
            shareAppFb(invitefburl);
        } else if (id == R.id.layout_share_whatsapp) {
            shareOnWhatsapp();
        } else if (id == R.id.layout_share_telegram) {
            shareOnTelegram();
        } else if (id == R.id.layout_share_more) {
            shareAppOther();
        }
    }
}
