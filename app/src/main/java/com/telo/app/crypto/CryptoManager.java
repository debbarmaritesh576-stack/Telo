package com.telo.app.crypto;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CryptoManager {

    private static final String TRANSFORM   = "AES/GCM/NoPadding";
    private static final int    GCM_TAG_BIT = 128;
    private static final int    IV_SIZE     = 12;

    // ── Encrypt ───────────────────────────────────────────────

    public static byte[] encrypt(String plaintext) throws Exception {
        SecretKey key = MasterKeyManager.getOrCreateMasterKey();
        byte[]    iv  = generateIv();

        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(Cipher.ENCRYPT_MODE, key,
            new GCMParameterSpec(GCM_TAG_BIT, iv));

        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

        // Prepend IV to encrypted data
        ByteBuffer buffer = ByteBuffer.allocate(IV_SIZE + encrypted.length);
        buffer.put(iv);
        buffer.put(encrypted);
        return buffer.array();
    }

    public static String encryptToBase64(String plaintext) throws Exception {
        byte[] encrypted = encrypt(plaintext);
        return android.util.Base64.encodeToString(
            encrypted, android.util.Base64.NO_WRAP
        );
    }

    // ── Decrypt ───────────────────────────────────────────────

    public static String decrypt(byte[] encryptedData) throws Exception {
        SecretKey key = MasterKeyManager.getOrCreateMasterKey();

        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
        byte[] iv        = new byte[IV_SIZE];
        byte[] encrypted = new byte[encryptedData.length - IV_SIZE];
        buffer.get(iv);
        buffer.get(encrypted);

        Cipher cipher = Cipher.getInstance(TRANSFORM);
        cipher.init(Cipher.DECRYPT_MODE, key,
            new GCMParameterSpec(GCM_TAG_BIT, iv));

        return new String(cipher.doFinal(encrypted), "UTF-8");
    }

    public static String decryptFromBase64(String base64) throws Exception {
        byte[] decoded = android.util.Base64.decode(
            base64, android.util.Base64.NO_WRAP
        );
        return decrypt(decoded);
    }

    // ── Helpers ───────────────────────────────────────────────

    private static byte[] generateIv() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static boolean isEncrypted(String value) {
        if (value == null || value.isEmpty()) return false;
        try {
            byte[] decoded = android.util.Base64.decode(
                value, android.util.Base64.NO_WRAP
            );
            return decoded.length > IV_SIZE;
        } catch (Exception e) {
            return false;
        }
    }
}