package com.telo.app.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.telo.app.R;
import com.telo.app.TeloApp;
import com.telo.app.ui.HomeActivity;

public class NotificationService {

    private static final int NOTIF_BREACH  = 2001;
    private static final int NOTIF_EXPIRY  = 2002;
    private static final int NOTIF_BACKUP  = 2003;

    private final Context context;

    public NotificationService(Context context) {
        this.context = context.getApplicationContext();
    }

    // ── Breach Alert ──────────────────────────────────────────

    public void showBreachAlert(String title, int breachCount) {
        String text = breachCount + " password(s) found in data breach!";

        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, TeloApp.CHANNEL_ALERT)
                .setContentTitle("⚠️ Security Alert — " + title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_breach)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(getMainPendingIntent());

        getManager().notify(NOTIF_BREACH, builder.build());
    }

    // ── Expiry Alert ──────────────────────────────────────────

    public void showExpiryAlert(int count) {
        String text = count + " password(s) expiring soon";

        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, TeloApp.CHANNEL_ALERT)
                .setContentTitle("Password Expiry Warning")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_timer)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(getMainPendingIntent());

        getManager().notify(NOTIF_EXPIRY, builder.build());
    }

    // ── Backup Reminder ───────────────────────────────────────

    public void showBackupReminder() {
        NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context, TeloApp.CHANNEL_BACKUP)
                .setContentTitle("Backup Reminder")
                .setContentText("Your last backup was over 7 days ago")
                .setSmallIcon(R.drawable.ic_backup)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setAutoCancel(true)
                .setContentIntent(getMainPendingIntent());

        getManager().notify(NOTIF_BACKUP, builder.build());
    }

    // ── Helpers ───────────────────────────────────────────────

    private NotificationManager getManager() {
        return (NotificationManager) context
            .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent getMainPendingIntent() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT |
            PendingIntent.FLAG_IMMUTABLE
        );
    }
}