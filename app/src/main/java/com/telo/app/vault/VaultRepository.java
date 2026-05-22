package com.telo.app.vault;

import android.content.Context;
import com.telo.app.otp.OTPEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VaultRepository {

    private final VaultManager vaultManager;

    public VaultRepository(Context context) {
        this.vaultManager = VaultManager.getInstance(context);
    }

    // ── OTP ───────────────────────────────────────────────────

    public List<OTPEntry> getAllOTPEntries() {
        List<OTPEntry> list = new ArrayList<>();
        for (VaultEntry ve : vaultManager.getEntries()) {
            if (ve.getEntryType() == VaultEntry.EntryType.OTP
                    && ve.getOtpEntry() != null) {
                list.add(ve.getOtpEntry());
            }
        }
        return list;
    }

    public List<OTPEntry> getOTPEntriesByCategory(String categoryId) {
        return getAllOTPEntries().stream()
            .filter(e -> categoryId.equals(e.getCategoryId()))
            .collect(Collectors.toList());
    }

    public List<OTPEntry> getFavoriteOTPEntries() {
        return getAllOTPEntries().stream()
            .filter(OTPEntry::isFavorite)
            .collect(Collectors.toList());
    }

    public List<OTPEntry> searchOTPEntries(String query) {
        String q = query.toLowerCase().trim();
        return getAllOTPEntries().stream()
            .filter(e ->
                (e.getName()   != null && e.getName().toLowerCase().contains(q)) ||
                (e.getIssuer() != null && e.getIssuer().toLowerCase().contains(q))
            )
            .collect(Collectors.toList());
    }

    public void addOTPEntry(OTPEntry entry) throws Exception {
        vaultManager.addOTPEntry(entry);
    }

    public void removeEntry(String id) throws Exception {
        vaultManager.removeEntry(id);
    }

    // ── Vault State ───────────────────────────────────────────

    public boolean isUnlocked() {
        return vaultManager.isUnlocked();
    }

    public boolean vaultExists() {
        return vaultManager.vaultExists();
    }

    public void lock() {
        vaultManager.lock();
    }

    public void panicWipe() {
        vaultManager.panicWipe();
    }
}