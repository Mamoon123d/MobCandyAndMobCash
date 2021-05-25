package com.cashhub.android.Services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;


public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the AdMob app

       /* MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        Map<String, String> map = sdk.getMTGConfigurationMap("123005", "882ac1406625edd424b56c30910b6945");
        sdk.init(map, this);*/



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel fcmChannel = new NotificationChannel(CHANNEL_ID,"FCM_Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(fcmChannel);
        }


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




}