package com.mobcash.android.Activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mobcash.android.Models.UserSignUpModel;
import com.mobcash.android.Interface.GetApiData;
import com.mobcash.android.R;
import com.mobcash.android.Services.PackageStatusReceiver;
import com.mobcash.android.Services.RetrofitClient;
import com.mobcash.android.Services.sharedConstant;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleLogin extends AppCompatActivity implements View.OnClickListener {

    protected static final String LOG_TAG = PackageStatusReceiver.class.getSimpleName();
    private InstallReferrerClient referrerClient;
    Context context;
    TextView textViewBtnLoginActivity,privacy,terms;
    private static final String TAG = "testing";
    SignInButton signIn;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    private GoogleSignInClient mGoogleSignInClient;
    String socialId, socialEmail, socialName, socialType = "google", deviceId, deviceName, apiLevel, versionName,UserTokenId;
    Uri socialImgurl;
    int versionCode;
    ProgressDialog progressDialog;

    public void onStart() {
        super.onStart();
        mAuth.getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_google_login);
            mAuth = FirebaseAuth.getInstance();

            textViewBtnLoginActivity = findViewById(R.id.btn_google_signin);
            privacy = findViewById(R.id.tvTerms);
            terms = findViewById(R.id.tvPrivacy);
            textViewBtnLoginActivity.setOnClickListener(this);
            privacy.setOnClickListener(this);
            terms.setOnClickListener(this);




            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            Log.d(LOG_TAG, "InstallReferrer Response.OK");
                            try {
                                ReferrerDetails response = referrerClient.getInstallReferrer();
                                String referrer = response.getInstallReferrer();


                                handleRefrellUrl(referrer);
                                long clickTimestamp = response.getReferrerClickTimestampSeconds();
                                long installTimestamp = response.getInstallBeginTimestampSeconds();
                                sharedConstant.setSharedPreferenceString(GoogleLogin.this, "referalUrl", referrer);

                                referrerClient.endConnection();
                            } catch (RemoteException e) {
                                Log.e(LOG_TAG, "" + e.getMessage());
                            }
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            Log.w(LOG_TAG, "InstallReferrer Response.FEATURE_NOT_SUPPORTED");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_UNAVAILABLE");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                            Log.w(LOG_TAG, "InstallReferrer Response.SERVICE_DISCONNECTED");
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                            Log.w(LOG_TAG, "InstallReferrer Response.DEVELOPER_ERROR");
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    Log.w(LOG_TAG, "InstallReferrer onInstallReferrerServiceDisconnected()");
                }
            });




            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("514351419756-q6qm5mhfd00j7t3sdhjibqtsj1bsu3kr.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
// Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mAuth = FirebaseAuth.getInstance();
            getIdThread1();
            showConsentDialog();

        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: GoogleLogin" + e);
        }
    }

    private void signIn() {
        Log.e(TAG, "signIn: ");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                UserTokenId = account.getIdToken();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            socialId = user.getUid();
                            socialEmail = user.getEmail();
                            socialName = user.getDisplayName();
                            socialImgurl = user.getPhotoUrl();
                            socialType = "Google";


                            Log.e(TAG, "onComplete: " + "Social id " + socialId + " socialEmail " + socialEmail + "socialName " + socialName + "socialImgurl" + socialImgurl);
                            apiLevel = String.valueOf(android.os.Build.VERSION.SDK_INT);
                            deviceId = Settings.Secure.getString(getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            deviceName = android.os.Build.MODEL;


                            try {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                versionName = pInfo.versionName;
                                versionCode = pInfo.versionCode;
                                sharedConstant.setSharedPreferenceString(GoogleLogin.this, "versionName", versionName);
                                sharedConstant.setSharedPreferenceInt(GoogleLogin.this, "versionCode", versionCode);
                                userSignUpDetails(deviceId, deviceName, socialType, socialId, socialEmail, socialName, socialImgurl, versionName, versionCode);


                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "onComplete: " + "deviceId " + deviceId + " VersionCode " + versionCode + "versionName " + versionName + "deviceName" + deviceName);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleLogin.this, "You Are Not Able To Login", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public void getIdThread1() {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                    Log.e(TAG, "doInBackground: " + idInfo);

                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
//                    advertisingId = advertId;
                    sharedConstant.setSharedPreferenceString(GoogleLogin.this, "adverId", advertId);
//                    Log.e("id", "getIdThread: "+advertId );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
//                Toast.makeText(getApplicationContext(), "advg: "+advertId, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();


    }

    private void userSignUpDetails(String deviceId, String deviceName, String socialType, String socialId, String socialEmail, String socialName, Uri socialImgurl, String versionName, int versionCode) {

        GetApiData getApiData = RetrofitClient.getRetrofitInstance().create(GetApiData.class);
        Call<UserSignUpModel> call = getApiData.getAllUsers(deviceId, deviceName, socialType, socialId, socialEmail, socialName, socialImgurl, versionName, versionCode,
                sharedConstant.getSharedPreferenceString(GoogleLogin.this, "utm_source", ""),
                sharedConstant.getSharedPreferenceString(GoogleLogin.this, "utm_medium", ""),
                sharedConstant.getSharedPreferenceString(GoogleLogin.this, "adverId", ""),
                sharedConstant.getSharedPreferenceString(GoogleLogin.this, "DeviceToken", ""),
                sharedConstant.getSharedPreferenceString(GoogleLogin.this,"referalUrl" , ""),UserTokenId);



        if (!GoogleLogin.this.isFinishing()) {
            progressDialog = new ProgressDialog(GoogleLogin.this);
            progressDialog.setMessage("Loading Wait..");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        call.enqueue(new Callback<UserSignUpModel>() {
            @Override
            public void onResponse(@NotNull Call<UserSignUpModel> call, @NotNull Response<UserSignUpModel> response) {
               dismissProgressDialog();
                if (response != null) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            int userId = response.body().getUserId();
                            String securityToken = response.body().getSecurityToken();
                            sharedConstant.setSharedPreferenceInt(GoogleLogin.this, "userId", userId);
                            sharedConstant.setSharedPreferenceString(GoogleLogin.this, "securitytoken", securityToken);
                            Intent intent = new Intent(GoogleLogin.this, UserProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(GoogleLogin.this, "Response:User" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(GoogleLogin.this, "Response:User" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserSignUpModel> call, @NotNull Throwable t) {
                dismissProgressDialog();
                Toast.makeText(GoogleLogin.this, getString(R.string.systemmessage) + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            FirebaseUser user = mAuth.getCurrentUser();

        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void showConsentDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.custom_consent_dialog_box, null);
        builder1.setView(dialogView);
//        builder1.setMessage(getString(R.string.permission_request));
        builder1.setCancelable(false);
        androidx.appcompat.app.AlertDialog alertdialog = builder1.create();
        LinearLayout ll_agreeLayout = dialogView.findViewById(R.id.agreeLayout);
        LinearLayout ll_exitAppLayout = dialogView.findViewById(R.id.exitAppLayout);
        TextView tv_privacyPolicy = dialogView.findViewById(R.id.tvPrivacy);
        TextView tv_consentMsg = dialogView.findViewById(R.id.tv_consent);

        tv_consentMsg.setText(getResources().getString(R.string.permissiondata));

        tv_privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inAppOpenBrowser("https://mobcash.app/privacypolicy.html");
            }
        });

        if (tv_consentMsg.getText().toString().contains("Privacy policy")) {
            setClickableHighLightedText(tv_consentMsg, "Privacy policy", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inAppOpenBrowser("https://www.mintegral.com/en/privacy/");
                }
            });
        }

        ll_agreeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdialog.dismiss();
            }
        });

        ll_exitAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdialog.dismiss();
                GoogleLogin.this.finish();
            }
        });


        alertdialog.show();
    }

    private void inAppOpenBrowser(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(GoogleLogin.this, R.color.colorPrimaryDark));
        builder.addDefaultShareMenuItem();

        CustomTabsIntent anotherCustomTab = new CustomTabsIntent.Builder().build();


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        int requestCode = 100;
        Intent intent = anotherCustomTab.intent;
        intent.setData(Uri.parse(url));

        PendingIntent pendingIntent = PendingIntent.getActivity(GoogleLogin.this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(bitmap, "Android", pendingIntent, true);
        builder.setShowTitle(true);


        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(GoogleLogin.this, Uri.parse(url));

    }


    private void setClickableHighLightedText(TextView tv_consentMsg, String clickable , View.OnClickListener onClickListener) {
        String tvt = tv_consentMsg.getText().toString();
        int ofe = tvt.indexOf(clickable, 0);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (onClickListener != null) onClickListener.onClick(textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(tv_consentMsg.getContext().getResources().getColor(R.color.linkcolor));
                ds.setUnderlineText(true);
            }
        };
        SpannableString wordToSpan = new SpannableString(tv_consentMsg.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(clickable, ofs);
            if (ofe == -1)
                break;
            else {
                wordToSpan.setSpan(clickableSpan, ofe, ofe + clickable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_consentMsg.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                tv_consentMsg.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void webViewLoad(String url, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(GoogleLogin.this);
        alert.setTitle(title);

        WebView wv = new WebView(GoogleLogin.this);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvPrivacy) {
            inAppOpenBrowser("https://mobcash.app/privacypolicy.html");
        } else if (i == R.id.tvTerms) {
            inAppOpenBrowser("https://mobcash.app/termncondition.html");
        } else if (i == R.id.btn_google_signin) {
            signIn();
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



    private void handleRefrellUrl(String referrerUrl) {
        if (referrerUrl != null) {
            String referrer = referrerUrl.substring(0, referrerUrl.length());
            String[] params = referrer.split("&");
            String utm_source = params[0].substring(params[0].lastIndexOf("=") + 1);
            String utm_medium = params[1].substring(params[1].lastIndexOf("=") + 1);
//            String utm_term = params[2].substring(params[2].lastIndexOf("=") + 1);
//            String utm_campaign = params[3].substring(params[3].lastIndexOf("=") + 1);
//            String utm_content = params[4].substring(params[4].lastIndexOf("=") + 1);
            Log.e("TAG", "handleRefrellUrl:utm_source " + utm_source);
            Log.e("TAG", "handleRefrellUrl:utm_medium " + utm_medium);
//            Log.e(TAG, "handleRefrellUrl:utm_term "+utm_term );
            sharedConstant.setSharedPreferenceString(GoogleLogin.this, "utm_source", utm_source);
            sharedConstant.setSharedPreferenceString(GoogleLogin.this, "utm_medium", utm_medium);

        }
    }

}