package com.example.android.maximfialko.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;

import com.example.android.maximfialko.MainActivity;
import com.example.android.maximfialko.R;
import com.example.android.maximfialko.service.StopNewsUpdateService;

import androidx.core.app.NotificationCompat;

public class NotificationBuilder {

    private static final String CHANNEL_ID = "news:notification:channel";
    private static final String CHANNEL_ID_RESULT = "news:notification:channel:pops";

    public static Notification createForegroundNotification(Context context) {

        if (VersionControl.atLeastOreo()) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_question)
                .setContentTitle("NY Times")
                .setContentText("News updating...")
                .addAction(cancelNewsUpdate(context))
//                .setContentIntent(openMainActivity(context))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return mBuilder.build();
    }

    private static NotificationCompat.Action cancelNewsUpdate(Context context) {
        Intent stopSelf = new Intent(context, StopNewsUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(),
                stopSelf, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(R.drawable.delete_forever, "Cancel", pendingIntent);
    }

    public static Notification showResultNotification(Context context, String message) {

        if (VersionControl.atLeastOreo()) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_RESULT, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID_RESULT)
                .setSmallIcon(R.drawable.ic_question)
                .setContentTitle("NY Times")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(openMainActivity(context))
                .setTicker(message)
                .setContentIntent(openMainActivity(context))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        return mBuilder.build();
    }

    public static PendingIntent openMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }
}
