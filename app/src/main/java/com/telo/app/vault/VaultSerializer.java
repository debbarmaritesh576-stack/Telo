package com.telo.app.vault;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telo.app.otp.OTPEntry;
import java.util.List;

public class VaultSerializer {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    // ── Serialize ─────────────────────────────────────────────

    public static String toJson(List<VaultEntry> entries) {
        return GSON.toJson(entries);
    }

    public static byte[] toBytes(List<VaultEntry> entries) {
        return toJson(entries).getBytes();
    }

    // ── Deserialize ───────────────────────────────────────────

    public static List<VaultEntry> fromJson(String json) {
        VaultEntry[] arr = GSON.fromJson(json, VaultEntry[].class);
        return java.util.Arrays.asList(arr);
    }

    public static List<VaultEntry> fromBytes(byte[] bytes) {
        return fromJson(new String(bytes));
    }

    // ── OTP Entry ─────────────────────────────────────────────

    public static String otpEntryToJson(OTPEntry entry) {
        return GSON.toJson(entry);
    }

    public static OTPEntry otpEntryFromJson(String json) {
        return GSON.fromJson(json, OTPEntry.class);
    }
}