package com.mobcash.android.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;


public class PackageStatusReceiver extends BroadcastReceiver implements InstallReferrerStateListener {

    protected static final String LOG_TAG = PackageStatusReceiver.class.getSimpleName();

    private InstallReferrerClient referrerClient;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null) {
            if(intent.getAction().equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH)) {
                this.referrerClient = InstallReferrerClient.newBuilder(context).build();
                this.referrerClient.startConnection(this);
            }
        }
    }

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
                    sharedConstant.setSharedPreferenceString(context, "referalUrl", referrer);
                    Log.d(LOG_TAG, "InstallReferrer " +sharedConstant.getSharedPreferenceString(context, "referalUrl", ""));
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

    private void handleRefrellUrl(String referrerUrl) {
        if (referrerUrl != null) {
            String referrer = referrerUrl.substring(0, referrerUrl.length());
            String params[] = referrer.split("&");
            String utm_source = params[0].substring(params[0].lastIndexOf("=") + 1);
            String utm_medium = params[1].substring(params[1].lastIndexOf("=") + 1);
//            String utm_term = params[2].substring(params[2].lastIndexOf("=") + 1);
//            String utm_campaign = params[3].substring(params[3].lastIndexOf("=") + 1);
//            String utm_content = params[4].substring(params[4].lastIndexOf("=") + 1);
//            Log.e("TAG", "handleRefrellUrl:utm_source " + utm_source);
//            Log.e("TAG", "handleRefrellUrl:utm_medium " + utm_medium);
//            Log.e(TAG, "handleRefrellUrl:utm_term "+utm_term );
            sharedConstant.setSharedPreferenceString(context, "utm_source", utm_source);
            sharedConstant.setSharedPreferenceString(context, "utm_medium", utm_medium);

        }
    }

}