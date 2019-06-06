package com.rpi_security;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mohamed on 04/04/2019.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String img_url = remoteMessage.getData().get("img_url");
        String date = remoteMessage.getData().get("date");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        InputStream in;
        Bitmap myBitmap = null;
        try {
            URL url = new URL(img_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(myBitmap).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
        builder.setContentTitle(title);
        builder.setContentText(message + date);
        builder.setContentIntent(pendingIntent);
        builder.setSound(sounduri);
        builder.setSmallIcon(R.mipmap.rpisecurity_icon);
        builder.setStyle(bigPictureStyle);
        builder.setColor(getResources().getColor(R.color.colorPrimary));
        //builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(myBitmap));
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            builder.setChannelId("com.rpi_security");
            NotificationChannel channel = new NotificationChannel(
                    "com.rpi_security",
                    "RPISecurity",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , builder.build());

        Log.d("TITLE", "Title : " + remoteMessage.getData().get("title"));
        Log.d("MSG", "Message : " + remoteMessage.getData().get("message"));
        Log.d("URL", "Url : " + remoteMessage.getData().get("img_url"));
    }
}