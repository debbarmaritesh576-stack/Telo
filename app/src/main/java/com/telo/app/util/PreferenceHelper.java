package com.telo.app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static final String PREFS_NAME = "telo_prefs";

    private static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        );
    }

    // ── App Settings ──────────────────────────────────────────

    public static boolean isFirstLaunch() {
        return prefs.getBoolean("first_launch", true);
    }

    public static void setFirstLaunchDone() {
        prefs.edit().putBoolean("first_launch", false).apply();
    }

    public static boolean isVaultSetup() {
        return prefs.getBoolean("vault_setup", false);
    }

    public static void setVaultSetup(boolean done) {
        prefs.edit().putBoolean("vault_setup", done).apply();
    }

    // ── Theme ─────────────────────────────────────────────────

    public static String getTheme() {
        return prefs.getString("theme", "system");
    }

    public static void setTheme(String theme) {
        prefs.edit().putString("theme", theme).apply();
    }

    // ── Security ──────────────────────────────────────────────

    public static boolean isBiometricEnabled() {
        return prefs.getBoolean("biometric_enabled", false);
    }

    public static void setBiometricEnabled(boolean enabled) {
        prefs.edit().putBoolean("biometric_enabled", enabled).apply();
    }

    public static boolean isScreenshotBlocked() {
        return prefs.getBoolean("screenshot_blocked", true);
    }

    public static void setScreenshotBlocked(boolean blocked) {
        prefs.edit().putBoolean("screenshot_blocked", blocked).apply();
    }

    public static long getLockTimeout() {
        return prefs.getLong("lock_timeout", 60);
    }

    public static void setLockTimeout(long seconds) {
        prefs.edit().putLong("lock_timeout", seconds).apply();
    }

    // ── Backup ────────────────────────────────────────────────

    public static boolean isAutoBackupEnabled() {
        return prefs.getBoolean("auto_backup", false);
    }

    public static void setAutoBackupEnabled(boolean enabled) {
        prefs.edit().putBoolean("auto_backup", enabled).apply();
    }

    public static long getLastBackupTime() {
        return prefs.getLong("last_backup", 0);
    }

    public static void setLastBackupTime(long time) {
        prefs.edit().putLong("last_backup", time).apply();
    }

    // ── Display ───────────────────────────────────────────────

    public static boolean isTapToReveal() {
        return prefs.getBoolean("tap_to_reveal", true);
    }

    public static void setTapToReveal(boolean enabled) {
        prefs.edit().putBoolean("tap_to_reveal", enabled).apply();
    }

    public static boolean isNextCodeVisible() {
        return prefs.getBoolean("next_code_visible", false);
    }

    public static void setNextCodeVisible(boolean visible) {
        prefs.edit().putBoolean("next_code_visible", visible).apply();
    }

    public static String getViewMode() {
        return prefs.getString("view_mode", "list");
    }

    public static void setViewMode(String mode) {
        prefs.edit().putString("view_mode", mode).apply();
    }
}