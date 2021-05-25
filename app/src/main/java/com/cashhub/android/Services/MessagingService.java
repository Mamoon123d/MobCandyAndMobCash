package com.cashhub.android.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.cashhub.android.Activities.SplashScreen;
import com.cashhub.android.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {


    Bitmap image;
    NotificationCompat.Builder notificationBuilder;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("testing", "onMessageReceived: " + remoteMessage.getNotification().getTitle());


        if (remoteMessage.getNotification() != null) {

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final Map<String, String> data = remoteMessage.getData();
            String mapData = data.get("OfferKey");

            Uri imgurl = remoteMessage.getNotification().getImageUrl();
            String imgs = String.valueOf(imgurl);
            image = getBitmapFromURL(imgs);

            sendNotification(remoteMessage.getNotification().getTag(),title,image,body,mapData);


        }
    }
    private void sendNotification(String tag, String title, Bitmap img,String messageBody,String mapData) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.putExtra("OfferKey",mapData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);*/
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelID = getString(R.string.channel_id);

        if (tag.equalsIgnoreCase("image")) {
            notificationBuilder = new NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.mipmap.ic_app_mobcash)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(img))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
              //      .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.mipmap.ic_app_mobcash)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
            //        .setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    @Override
    public void onNewToken(String s) {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        sharedConstant.setSharedPreferenceString(MessagingService.this,"DeviceToken",deviceToken);
        Log.d("testing",deviceToken);
    }
}
