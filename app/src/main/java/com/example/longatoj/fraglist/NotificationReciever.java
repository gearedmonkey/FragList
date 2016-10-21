package com.example.longatoj.fraglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

public class NotificationReciever extends BroadcastReceiver {
    public NotificationReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("notification", "starting notification process...");
        Intent i = new Intent(context, MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);
        Notification n  = new Notification.Builder(context)
                .setContentTitle("Todo list")
                .setContentText("Did you remember to do your thing?")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }
}
