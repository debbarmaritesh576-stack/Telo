package com.telo.app.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HMACManager {

    // ── HMAC-SHA256 ───────────────────────────────────────────

    public static byte[] hmacSha256(
            byte[] key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance(CryptoConstants.HMAC_SHA256);
        mac.init(new SecretKeySpec(key, CryptoConstants.HMAC_SHA256));
        return mac.doFinal(data);
    }

    public static String hmacSha256Hex(
            byte[] key, String data) throws Exception {
        byte[] result = hmacSha256(
            key, data.getBytes(StandardCharsets.UTF_8)
        );
        return HashManager.bytesToHex(result);
    }

    // ── HMAC-SHA512 ───────────────────────────────────────────

    public static byte[] hmacSha512(
            byte[] key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance(CryptoConstants.HMAC_SHA512);
        mac.init(new SecretKeySpec(key, CryptoConstants.HMAC_SHA512));
        return mac.doFinal(data);
    }

    // ── Verify ────────────────────────────────────────────────

    public static boolean verify(
            byte[] key,
            byte[] data,
            byte[] expectedHmac) throws Exception {
        byte[] actual = hmacSha256(key, data);
        return HashManager.safeEquals(actual, expectedHmac);
    }
}