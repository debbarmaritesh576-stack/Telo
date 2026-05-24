package com.telo.app.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.telo.app.R;
import com.telo.app.TeloApp;
import com.telo.app.ui.HomeActivity;

public class NotificationHelper {

    public static void showSimple(
            Context context,
            int     notifId,
            String  channelId,
            String  title,
            String  message) {

        PendingIntent intent = PendingIntent.getActivity(
            context, 0,
            new Intent(context, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                          Intent.FLAG_ACTIVITY_CLEAR_TASK),
            PendingIntent.FLAG_UPDATE_CURRENT |
            PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_lock)
                .setAutoCancel(true)
                .setContentIntent(intent);

        NotificationManager nm =
            (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE
            );
        if (nm != null) nm.notify(notifId, builder.build());
    }

    public static void cancel(Context context, int notifId) {
        NotificationManager nm =
            (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE
            );
        if (nm != null) nm.cancel(notifId);
    }
}