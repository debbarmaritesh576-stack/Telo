package com.telo.app.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashManager {

    // ── SHA-256 ───────────────────────────────────────────────

    public static byte[] sha256(byte[] data) throws Exception {
        return MessageDigest
            .getInstance(CryptoConstants.SHA256_ALGORITHM)
            .digest(data);
    }

    public static String sha256Hex(String input) throws Exception {
        byte[] hash = sha256(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    // ── SHA-512 ───────────────────────────────────────────────

    public static byte[] sha512(byte[] data) throws Exception {
        return MessageDigest
            .getInstance(CryptoConstants.SHA512_ALGORITHM)
            .digest(data);
    }

    public static String sha512Hex(String input) throws Exception {
        byte[] hash = sha512(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    // ── Compare ───────────────────────────────────────────────

    public static boolean safeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length)   return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    // ── Hex ───────────────────────────────────────────────────

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String hex) {
        int    len  = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i),     16) << 4)
                                +  Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}