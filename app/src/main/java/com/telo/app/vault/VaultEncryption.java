package com.telo.app.vault;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

public class VaultEncryption {

    private static final String KEYSTORE        = "AndroidKeyStore";
    private static final String TRANSFORM       = "AES/GCM/NoPadding";
    private static final int    GCM_TAG_BIT     = 128;
    private static final int    IV_SIZE         = 12;
    private static final int    KEY_SIZE        = 256;

    // ── Master Key Generation ─────────────────────────────────

    public static byte[] generateMasterKey() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return key;
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static byte[] generateIv() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // ── Password → Key Derivation (PBKDF2) ───────────────────

    public static byte[] deriveKeyFromPassword(
            char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
            password, salt, 100000, KEY_SIZE
        );
        SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256");
        byte[] key = factory.generateSecret(spec).getEncoded();
        spec.clearPassword();
        return key;
    }

    // ── AES-GCM Encrypt ──────────────────────────────────────

    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv)
            throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(
            Cipher.ENCRYPT_MODE,
            new SecretKeySpec(key, "AES"),
            new GCMParameterSpec(GCM_TAG_BIT, iv)
        );
        return cipher.doFinal(data);
    }

    // ── AES-GCM Decrypt ──────────────────────────────────────

    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv)
            throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(
            Cipher.DECRYPT_MODE,
            new SecretKeySpec(key, "AES"),
            new GCMParameterSpec(GCM_TAG_BIT, iv)
        );
        return cipher.doFinal(data);
    }

    // ── Android Keystore (Biometric) ─────────────────────────

    public static void generateBiometricKey(String alias) throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        ks.load(null);
        if (ks.containsAlias(alias)) return;

        KeyGenerator kg = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, KEYSTORE
        );
        kg.init(new KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .setUserAuthenticationRequired(true)
            .build()
        );
        kg.generateKey();
    }

    public static Cipher getBiometricEncryptCipher(String alias) throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        ks.load(null);
        SecretKey key    = (SecretKey) ks.getKey(alias, null);
        Cipher    cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getBiometricDecryptCipher(String alias, byte[] iv)
            throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        ks.load(null);
        SecretKey key    = (SecretKey) ks.getKey(alias, null);
        Cipher    cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(
            Cipher.DECRYPT_MODE, key,
            new GCMParameterSpec(GCM_TAG_BIT, iv)
        );
        return cipher;
    }

    // ── Secure Wipe ───────────────────────────────────────────

    public static void wipeBytes(byte[] data) {
        if (data != null) Arrays.fill(data, (byte) 0);
    }
}