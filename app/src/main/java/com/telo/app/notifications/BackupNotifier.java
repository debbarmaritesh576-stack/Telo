package com.telo.app.notifications;

import android.content.Context;
import com.telo.app.TeloApp;

public class BackupNotifier {

    private static final int NOTIF_ID = 3003;

    public static void notifyReminder(Context context) {
        NotificationHelper.showSimple(
            context,
            NOTIF_ID,
            TeloApp.CHANNEL_BACKUP,
            "💾 Backup Reminder",
            "Your last backup was over 7 days ago"
        );
    }

    public static void notifySuccess(Context context) {
        NotificationHelper.showSimple(
            context,
            NOTIF_ID,
            TeloApp.CHANNEL_BACKUP,
            "✅ Backup Complete",
            "Your Telo vault has been backed up successfully"
        );
    }

    public static void notifyFailed(Context context) {
        NotificationHelper.showSimple(
            context,
            NOTIF_ID,
            TeloApp.CHANNEL_BACKUP,
            "❌ Backup Failed",
            "Could not complete backup — try again"
        );
    }

    public static void cancel(Context context) {
        NotificationHelper.cancel(context, NOTIF_ID);
    }
}