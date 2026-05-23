package com.telo.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.telo.app.security.AutoLockManager;
import com.telo.app.vault.VaultManager;

public class AutoLockService extends Service {

    private static final long CHECK_INTERVAL_MS = 10_000; // 10 seconds

    private Handler  handler;
    private Runnable checkRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        startChecking();
    }

    private void startChecking() {
        checkRunnable = new Runnable() {
            @Override
            public void run() {
                checkAndLock();
                handler.postDelayed(this, CHECK_INTERVAL_MS);
            }
        };
        handler.postDelayed(checkRunnable, CHECK_INTERVAL_MS);
    }

    private void checkAndLock() {
        AutoLockManager lockManager =
            AutoLockManager.getInstance(this);

        if (lockManager.shouldLock() &&
            VaultManager.getInstance(this).isUnlocked()) {
            VaultManager.getInstance(this).lock();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && checkRunnable != null) {
            handler.removeCallbacks(checkRunnable);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}