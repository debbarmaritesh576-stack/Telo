package com.telo.app.passwords;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PasswordExpiryChecker {

    public enum ExpiryStatus {
        OK,
        EXPIRING_SOON,  // 7 din baaki
        EXPIRED
    }

    public static class ExpiryResult {
        public final PasswordEntry entry;
        public final ExpiryStatus  status;
        public final long          daysRemaining;

        public ExpiryResult(PasswordEntry entry,
                            ExpiryStatus status,
                            long daysRemaining) {
            this.entry         = entry;
            this.status        = status;
            this.daysRemaining = daysRemaining;
        }
    }

    private static final long EXPIRY_DAYS      = 90;  // 90 din baad expire
    private static final long WARNING_DAYS     = 7;   // 7 din pehle warn

    public static ExpiryStatus check(PasswordEntry entry) {
        long changedAt   = entry.getPasswordChangedAt();
        long now         = System.currentTimeMillis();
        long ageMs       = now - changedAt;
        long ageDays     = TimeUnit.MILLISECONDS.toDays(ageMs);
        long daysLeft    = EXPIRY_DAYS - ageDays;

        if (daysLeft <= 0)           return ExpiryStatus.EXPIRED;
        if (daysLeft <= WARNING_DAYS) return ExpiryStatus.EXPIRING_SOON;
        return ExpiryStatus.OK;
    }

    public static List<ExpiryResult> checkAll(List<PasswordEntry> entries) {
        List<ExpiryResult> results = new ArrayList<>();
        for (PasswordEntry entry : entries) {
            long changedAt  = entry.getPasswordChangedAt();
            long now        = System.currentTimeMillis();
            long ageDays    = TimeUnit.MILLISECONDS.toDays(now - changedAt);
            long daysLeft   = EXPIRY_DAYS - ageDays;
            ExpiryStatus status = check(entry);
            results.add(new ExpiryResult(entry, status, daysLeft));
        }
        return results;
    }

    public static List<PasswordEntry> getExpired(List<PasswordEntry> entries) {
        List<PasswordEntry> list = new ArrayList<>();
        for (PasswordEntry e : entries) {
            if (check(e) == ExpiryStatus.EXPIRED) list.add(e);
        }
        return list;
    }

    public static List<PasswordEntry> getExpiringSoon(List<PasswordEntry> entries) {
        List<PasswordEntry> list = new ArrayList<>();
        for (PasswordEntry e : entries) {
            if (check(e) == ExpiryStatus.EXPIRING_SOON) list.add(e);
        }
        return list;
    }
}