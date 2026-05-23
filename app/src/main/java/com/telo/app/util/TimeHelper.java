package com.telo.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeHelper {

    // ── Format ────────────────────────────────────────────────

    public static String formatDate(long timestamp) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(new Date(timestamp));
    }

    public static String formatDateTime(long timestamp) {
        return new SimpleDateFormat(
            "dd MMM yyyy, HH:mm", Locale.getDefault()
        ).format(new Date(timestamp));
    }

    public static String formatRelative(long timestamp) {
        long diff = System.currentTimeMillis() - timestamp;

        if (diff < TimeUnit.MINUTES.toMillis(1)) {
            return "Just now";
        } else if (diff < TimeUnit.HOURS.toMillis(1)) {
            long mins = TimeUnit.MILLISECONDS.toMinutes(diff);
            return mins + " min ago";
        } else if (diff < TimeUnit.DAYS.toMillis(1)) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + " hr ago";
        } else if (diff < TimeUnit.DAYS.toMillis(30)) {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + " days ago";
        } else {
            return formatDate(timestamp);
        }
    }

    // ── Password Age ──────────────────────────────────────────

    public static long getDaysOld(long timestamp) {
        return TimeUnit.MILLISECONDS.toDays(
            System.currentTimeMillis() - timestamp
        );
    }

    public static boolean isOlderThan(long timestamp, long days) {
        return getDaysOld(timestamp) > days;
    }

    // ── OTP Timer ─────────────────────────────────────────────

    public static long getCurrentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getRemainingSeconds(long period) {
        long now = getCurrentTimeSeconds();
        return period - (now % period);
    }

    public static float getProgress(long period) {
        long now     = getCurrentTimeSeconds();
        long elapsed = now % period;
        return 1f - ((float) elapsed / period);
    }
}