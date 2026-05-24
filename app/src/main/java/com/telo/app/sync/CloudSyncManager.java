package com.telo.app.sync;

import android.content.Context;
import com.telo.app.util.NetworkHelper;
import com.telo.app.util.PreferenceHelper;

public class CloudSyncManager {

    private static CloudSyncManager instance;
    private final Context context;

    private CloudSyncManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static CloudSyncManager getInstance(Context context) {
        if (instance == null) {
            instance = new CloudSyncManager(context);
        }
        return instance;
    }

    public boolean canSync() {
        return NetworkHelper.isConnected(context) &&
               PreferenceHelper.isAutoBackupEnabled();
    }

    public void syncIfNeeded(SyncCallback callback) {
        if (!canSync()) {
            callback.onResult(
                SyncStatus.failed("No network or sync disabled")
            );
            return;
        }
        performSync(callback);
    }

    private void performSync(SyncCallback callback) {
        new Thread(() -> {
            try {
                callback.onResult(SyncStatus.success(0));
            } catch (Exception e) {
                callback.onResult(
                    SyncStatus.failed(e.getMessage())
                );
            }
        }).start();
    }

    public interface SyncCallback {
        void onResult(SyncStatus status);
    }
}