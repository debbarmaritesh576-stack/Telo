package com.telo.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.telo.app.R;
import com.telo.app.TeloApp;
import com.telo.app.exporters.EncryptedExporter;
import com.telo.app.exporters.ExportResult;
import com.telo.app.otp.OTPEntry;
import com.telo.app.util.PreferenceHelper;
import java.util.List;

public class BackupService extends Service {

    public static final String ACTION_BACKUP  = "com.telo.app.action.BACKUP";
    public static final String EXTRA_URI      = "backup_uri";
    public static final String EXTRA_PASSWORD = "backup_password";

    private static final int NOTIF_ID = 1001;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;

        String action = intent.getAction();
        if (ACTION_BACKUP.equals(action)) {
            Uri    uri      = intent.getParcelableExtra(EXTRA_URI);
            String password = intent.getStringExtra(EXTRA_PASSWORD);
            startForeground(NOTIF_ID, buildNotification("Backing up..."));
            performBackup(uri, password);
        }

        return START_NOT_STICKY;
    }

    private void performBackup(Uri uri, String password) {
        new Thread(() -> {
            try {
                EncryptedExporter exporter = new EncryptedExporter(
                    this, password.toCharArray()
                );

                // TODO: get entries from repository
                // ExportResult result = exporter.exportOTP(entries, uri);

                PreferenceHelper.setLastBackupTime(
                    System.currentTimeMillis()
                );

                updateNotification("Backup complete");

            } catch (Exception e) {
                updateNotification("Backup failed: " + e.getMessage());
            } finally {
                stopSelf();
            }
        }).start();
    }

    private Notification buildNotification(String text) {
        return new NotificationCompat.Builder(
            this, TeloApp.CHANNEL_BACKUP)
            .setContentTitle("Telo Backup")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_backup)
            .setOngoing(true)
            .build();
    }

    private void updateNotification(String text) {
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm != null) nm.notify(NOTIF_ID, buildNotification(text));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}