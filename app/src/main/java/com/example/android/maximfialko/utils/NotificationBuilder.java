package com.example.android.maximfialko.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.android.maximfialko.R;

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
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return mBuilder.build();
    }


}
