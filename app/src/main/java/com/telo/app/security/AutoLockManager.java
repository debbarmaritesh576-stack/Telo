package com.telo.app.security;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.telo.app.vault.VaultManager;

public class AutoLockManager {

    public enum LockTimeout {
        IMMEDIATELY(0),
        SEC_30(30),
        MIN_1(60),
        MIN_5(300),
        MIN_15(900),
        MIN_30(1800),
        NEVER(-1);

        private final long seconds;

        LockTimeout(long seconds) {
            this.seconds = seconds;
        }

        public long getSeconds() { return seconds; }
    }

    private static final String PREFS_NAME      = "telo_security";
    private static final String KEY_TIMEOUT     = "lock_timeout";
    private static final String KEY_LAST_ACTIVE = "last_active";

    private static AutoLockManager instance;
    private final SharedPreferences prefs;
    private final Context           context;

    private AutoLockManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs   = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        );
    }

    public static AutoLockManager getInstance(Context context) {
        if (instance == null) {
            instance = new AutoLockManager(context);
        }
        return instance;
    }

    // ── Timeout Settings ──────────────────────────────────────

    public void setLockTimeout(LockTimeout timeout) {
        prefs.edit()
             .putLong(KEY_TIMEOUT, timeout.getSeconds())
             .apply();
    }

    public LockTimeout getLockTimeout() {
        long seconds = prefs.getLong(KEY_TIMEOUT, 60);
        for (LockTimeout t : LockTimeout.values()) {
            if (t.getSeconds() == seconds) return t;
        }
        return LockTimeout.MIN_1;
    }

    // ── Activity Tracking ─────────────────────────────────────

    public void onUserActivity() {
        prefs.edit()
             .putLong(KEY_LAST_ACTIVE, System.currentTimeMillis())
             .apply();
    }

    public void onAppBackground() {
        prefs.edit()
             .putLong(KEY_LAST_ACTIVE, System.currentTimeMillis())
             .apply();
    }

    public void onAppForeground() {
        if (shouldLock()) {
            VaultManager.getInstance(context).lock();
        }
    }

    // ── Lock Check ────────────────────────────────────────────

    public boolean shouldLock() {
        long timeoutSec = getLockTimeout().getSeconds();
        if (timeoutSec == -1) return false;  // Never
        if (timeoutSec ==  0) return true;   // Immediately

        long lastActive = prefs.getLong(KEY_LAST_ACTIVE, 0);
        long elapsedSec = (System.currentTimeMillis() - lastActive) / 1000;
        return elapsedSec >= timeoutSec;
    }

    public boolean isLocked() {
        return !VaultManager.getInstance(context).isUnlocked();
    }
}