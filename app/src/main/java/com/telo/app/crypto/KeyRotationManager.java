package com.telo.app.crypto;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyRotationManager {

    private static final String PREFS_NAME    = "telo_key_rotation";
    private static final String KEY_VERSION   = "key_version";
    private static final String KEY_ROTATED_AT = "key_rotated_at";

    private final SharedPreferences prefs;

    public KeyRotationManager(Context context) {
        this.prefs = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        );
    }

    // ── Version Tracking ──────────────────────────────────────

    public int getCurrentKeyVersion() {
        return prefs.getInt(KEY_VERSION, 1);
    }

    public void incrementKeyVersion() {
        int current = getCurrentKeyVersion();
        prefs.edit()
             .putInt(KEY_VERSION,     current + 1)
             .putLong(KEY_ROTATED_AT, System.currentTimeMillis())
             .apply();
    }

    public long getLastRotatedAt() {
        return prefs.getLong(KEY_ROTATED_AT, 0);
    }

    // ── Rotation ──────────────────────────────────────────────

    public void rotateKey() throws Exception {
        // Delete old master key
        KeyStoreManager.deleteKey(CryptoConstants.MASTER_KEY_ALIAS);

        // Generate new master key
        KeyStoreManager.getMasterKey();

        // Increment version
        incrementKeyVersion();
    }

    public boolean needsRotation(long maxAgeMs) {
        long lastRotated = getLastRotatedAt();
        if (lastRotated == 0) return false;
        return System.currentTimeMillis() - lastRotated > maxAgeMs;
    }
}