package com.telo.app.security;

import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordExpiryChecker;
import com.telo.app.passwords.PasswordStrengthChecker;
import com.telo.app.util.PreferenceHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityAnalyzer {

    // ── Password Analysis ─────────────────────────────────────

    public static int analyzePasswords(List<PasswordEntry> entries) {
        if (entries == null || entries.isEmpty()) return 50;

        int score        = 0;
        int totalChecks  = entries.size() * 3;
        int passedChecks = 0;

        for (PasswordEntry entry : entries) {
            // Strength check
            if (entry.getPassword() != null) {
                PasswordStrengthChecker.Result result =
                    PasswordStrengthChecker.check(entry.getPassword());
                if (result.strength == PasswordStrengthChecker.Strength.STRONG ||
                    result.strength == PasswordStrengthChecker.Strength.VERY_STRONG) {
                    passedChecks++;
                }
            }

            // Expiry check
            if (PasswordExpiryChecker.check(entry) ==
                    PasswordExpiryChecker.ExpiryStatus.OK) {
                passedChecks++;
            }

            // Has 2FA linked
            if (entry.isHasTotp()) {
                passedChecks++;
            }
        }

        score = (int) ((passedChecks / (float) totalChecks) * 100);
        return Math.min(100, Math.max(0, score));
    }

    // ── Duplicate Detection ───────────────────────────────────

    public static List<List<PasswordEntry>> findDuplicates(
            List<PasswordEntry> entries) {
        Map<String, List<PasswordEntry>> groups = new HashMap<>();

        for (PasswordEntry entry : entries) {
            if (entry.getPassword() == null) continue;
            String key = entry.getPassword();
            groups.computeIfAbsent(key, k -> new ArrayList<>())
                  .add(entry);
        }

        List<List<PasswordEntry>> duplicates = new ArrayList<>();
        for (List<PasswordEntry> group : groups.values()) {
            if (group.size() > 1) duplicates.add(group);
        }
        return duplicates;
    }

    // ── Weak Password Detection ───────────────────────────────

    public static List<PasswordEntry> findWeak(
            List<PasswordEntry> entries) {
        List<PasswordEntry> weak = new ArrayList<>();
        for (PasswordEntry entry : entries) {
            if (entry.getPassword() == null) continue;
            PasswordStrengthChecker.Result result =
                PasswordStrengthChecker.check(entry.getPassword());
            if (result.strength == PasswordStrengthChecker.Strength.VERY_WEAK ||
                result.strength == PasswordStrengthChecker.Strength.WEAK) {
                weak.add(entry);
            }
        }
        return weak;
    }

    // ── Backup Score ──────────────────────────────────────────

    public static int analyzeBackup() {
        long lastBackup = PreferenceHelper.getLastBackupTime();
        if (lastBackup == 0) return 0;

        long daysSinceBackup = (System.currentTimeMillis() - lastBackup)
            / (1000 * 60 * 60 * 24);

        if (daysSinceBackup <= 7)  return 100;
        if (daysSinceBackup <= 14) return 75;
        if (daysSinceBackup <= 30) return 50;
        if (daysSinceBackup <= 60) return 25;
        return 0;
    }

    // ── Vault Score ───────────────────────────────────────────

    public static int analyzeVault() {
        int score = 0;
        if (PreferenceHelper.isBiometricEnabled()) score += 40;
        if (PreferenceHelper.isScreenshotBlocked()) score += 30;
        score += 30; // password protected vault
        return score;
    }

    // ── OTP Score ─────────────────────────────────────────────

    public static int analyzeOTP(int otpCount) {
        if (otpCount == 0) return 20;
        if (otpCount < 3)  return 50;
        if (otpCount < 10) return 80;
        return 100;
    }

    // ── Full Analysis ─────────────────────────────────────────

    public static SecurityScore analyze(
            List<PasswordEntry> passwords,
            int otpCount) {
        SecurityScore score = new SecurityScore();
        score.setPasswordScore(analyzePasswords(passwords));
        score.setOtpScore(analyzeOTP(otpCount));
        score.setBackupScore(analyzeBackup());
        score.setVaultScore(analyzeVault());
        score.calculate();
        return score;
    }
}