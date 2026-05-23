package com.telo.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.telo.app.services.BackupService;
import com.telo.app.util.PreferenceHelper;
import com.telo.app.util.NetworkHelper;
import java.util.concurrent.TimeUnit;

public class BackupReceiver extends BroadcastReceiver {

    private static final long BACKUP_INTERVAL_MS =
        TimeUnit.DAYS.toMillis(7);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!PreferenceHelper.isAutoBackupEnabled()) return;

        long lastBackup = PreferenceHelper.getLastBackupTime();
        long now        = System.currentTimeMillis();

        if (now - lastBackup < BACKUP_INTERVAL_MS) return;

        if (!NetworkHelper.isConnected(context)) return;

        Intent serviceIntent = new Intent(
            context, BackupService.class
        );
        serviceIntent.setAction(BackupService.ACTION_BACKUP);
        context.startService(serviceIntent);
    }
}