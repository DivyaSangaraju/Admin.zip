package com.tripnetra.tnadmin.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.bookings.Marriage_Add_Rsp;
import com.tripnetra.tnadmin.bookings.Todo_list_details;

import java.util.Date;

public class FBase_Messaging_Service extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    String pnr ;
    String id  ;
    String status ;
    String Type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String Title = remoteMessage.getNotification().getTitle();
        String Message = remoteMessage.getNotification().getBody();
        String Action = remoteMessage.getNotification().getClickAction();



        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            pnr = remoteMessage.getData().get("pnr");
            id = remoteMessage.getData().get("fid");
            Type=remoteMessage.getData().get("type");

        }

        assert Type != null;
        sendNotification(Title, Message, Action,pnr, id,Type);

    }

    private void sendNotification(String Title, String Message,String Action, String pnr, String id,String Type) {



        Bundle b = new Bundle();
        Intent intent;
        b.putString("pnr",pnr);
        b.putString("fid",id);

        Log.i("b", String.valueOf(b));

        Log.i("ty",Type);

//        Toast.makeText(this, Type, Toast.LENGTH_SHORT).show();

        if(Type.equals("marriage")){
            intent = new Intent(this, Marriage_Add_Rsp.class);
            intent.putExtras(b);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else{

            intent = new Intent(this, Todo_list_details.class);
            intent.putExtras(b);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),
                    "Extranettest", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Title);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo_white)
                .setContentTitle(Title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setSound(Sound)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLights(Color.YELLOW, 1000, 300);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        assert notificationManager != null;
        notificationManager.notify(m, builder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("tk",s);
    }
}