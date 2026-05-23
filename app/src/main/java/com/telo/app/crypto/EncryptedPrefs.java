package com.telo.app.crypto;

import android.content.Context;
import android.content.SharedPreferences;

public class EncryptedPrefs {

    private static final String PREFS_NAME = "telo_encrypted_prefs";
    private final SharedPreferences prefs;

    public EncryptedPrefs(Context context) {
        this.prefs = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        );
    }

    // ── Write ─────────────────────────────────────────────────

    public void putString(String key, String value) {
        try {
            String encrypted = CryptoManager.encryptToBase64(value);
            prefs.edit().putString(key, encrypted).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putBoolean(String key, boolean value) {
        putString(key, String.valueOf(value));
    }

    public void putInt(String key, int value) {
        putString(key, String.valueOf(value));
    }

    public void putLong(String key, long value) {
        putString(key, String.valueOf(value));
    }

    // ── Read ──────────────────────────────────────────────────

    public String getString(String key, String defaultValue) {
        try {
            String encrypted = prefs.getString(key, null);
            if (encrypted == null) return defaultValue;
            return CryptoManager.decryptFromBase64(encrypted);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try { return Integer.parseInt(value); }
        catch (Exception e) { return defaultValue; }
    }

    public long getLong(String key, long defaultValue) {
        String value = getString(key, null);
        if (value == null) return defaultValue;
        try { return Long.parseLong(value); }
        catch (Exception e) { return defaultValue; }
    }

    // ── Remove ────────────────────────────────────────────────

    public void remove(String key) {
        prefs.edit().remove(key).apply();
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}