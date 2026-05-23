package com.telo.app.crypto;

public final class CryptoConstants {

    private CryptoConstants() {}

    // ── AES-GCM ───────────────────────────────────────────────
    public static final String AES_ALGORITHM    = "AES";
    public static final String AES_TRANSFORM    = "AES/GCM/NoPadding";
    public static final int    AES_KEY_SIZE     = 256;
    public static final int    GCM_TAG_BIT      = 128;
    public static final int    IV_SIZE          = 12;

    // ── Key Derivation ────────────────────────────────────────
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int    PBKDF2_ITERATIONS = 100_000;
    public static final int    PBKDF2_KEY_LENGTH = 256;
    public static final int    SALT_SIZE         = 32;

    // ── Keystore ──────────────────────────────────────────────
    public static final String KEYSTORE_PROVIDER  = "AndroidKeyStore";
    public static final String MASTER_KEY_ALIAS   = "telo_master_key";
    public static final String BIOMETRIC_KEY_ALIAS = "telo_biometric_key";
    public static final String PIN_KEY_ALIAS       = "telo_pin_key";

    // ── Hashing ───────────────────────────────────────────────
    public static final String SHA256_ALGORITHM  = "SHA-256";
    public static final String SHA512_ALGORITHM  = "SHA-512";
    public static final String HMAC_SHA256       = "HmacSHA256";
    public static final String HMAC_SHA512       = "HmacSHA512";

    // ── Clipboard ─────────────────────────────────────────────
    public static final long   CLIPBOARD_CLEAR_DELAY_MS = 30_000; // 30s

    // ── PIN ───────────────────────────────────────────────────
    public static final int    PIN_MIN_LENGTH    = 4;
    public static final int    PIN_MAX_LENGTH    = 8;
    public static final int    MAX_PIN_ATTEMPTS  = 5;
    public static final long   PIN_LOCKOUT_MS    = 30_000;

    // ── Backup ────────────────────────────────────────────────
    public static final String BACKUP_FILE_EXT   = ".telo";
    public static final int    BACKUP_VERSION     = 2;
}