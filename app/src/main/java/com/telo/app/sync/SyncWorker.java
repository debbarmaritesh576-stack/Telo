package com.telo.app.sync;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class SyncWorker extends Worker {

    public static final String TAG = "TeloSyncWorker";

    public SyncWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            CloudSyncManager manager =
                CloudSyncManager.getInstance(getApplicationContext());

            if (!manager.canSync()) {
                return Result.retry();
            }

            final boolean[] success = {false};
            manager.syncIfNeeded(status -> {
                success[0] = status.isSuccess();
            });

            Thread.sleep(2000);
            return success[0] ? Result.success() : Result.retry();

        } catch (Exception e) {
            return Result.failure();
        }
    }

    public static PeriodicWorkRequest buildRequest() {
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build();

        return new PeriodicWorkRequest.Builder(
            SyncWorker.class, 24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(TAG)
            .build();
    }
}