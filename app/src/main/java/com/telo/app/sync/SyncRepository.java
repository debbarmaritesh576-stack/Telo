package com.telo.app.sync;

import android.content.Context;
import androidx.work.WorkManager;
import com.telo.app.util.PreferenceHelper;

public class SyncRepository {

    private final CloudSyncManager syncManager;
    private final WorkManager      workManager;

    public SyncRepository(Context context) {
        syncManager = CloudSyncManager.getInstance(context);
        workManager = WorkManager.getInstance(context);
    }

    public void enableAutoSync() {
        PreferenceHelper.setAutoBackupEnabled(true);
        workManager.enqueueUniquePeriodicWork(
            SyncWorker.TAG,
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            SyncWorker.buildRequest()
        );
    }

    public void disableAutoSync() {
        PreferenceHelper.setAutoBackupEnabled(false);
        workManager.cancelUniqueWork(SyncWorker.TAG);
    }

    public void syncNow(CloudSyncManager.SyncCallback callback) {
        syncManager.syncIfNeeded(callback);
    }

    public boolean isAutoSyncEnabled() {
        return PreferenceHelper.isAutoBackupEnabled();
    }
}