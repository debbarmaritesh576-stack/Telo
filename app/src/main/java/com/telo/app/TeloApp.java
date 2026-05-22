package com.telo.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class TeloApp extends Application {

    private static TeloApp instance;

    public static final String CHANNEL_BACKUP = "telo_backup";
    public static final String CHANNEL_ALERT  = "telo_alert";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createNotificationChannels();
    }

    public static TeloApp getInstance() {
        return instance;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = getSystemService(NotificationManager.class);

            NotificationChannel backupChannel = new NotificationChannel(
                CHANNEL_BACKUP,
                "Backup",
                NotificationManager.IMPORTANCE_LOW
            );

            NotificationChannel alertChannel = new NotificationChannel(
                CHANNEL_ALERT,
                "Security Alerts",
                NotificationManager.IMPORTANCE_HIGH
            );

            nm.createNotificationChannel(backupChannel);
            nm.createNotificationChannel(alertChannel);
        }
    }
}