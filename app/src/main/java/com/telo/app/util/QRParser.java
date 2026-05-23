package com.telo.app.util;

import android.net.Uri;
import com.telo.app.otp.OTPAlgorithm;
import com.telo.app.otp.OTPEntry;
import com.telo.app.otp.OTPType;

public class QRParser {

    public static OTPEntry parse(String uri) throws Exception {
        if (uri == null || uri.isEmpty()) {
            throw new Exception("Empty URI");
        }

        Uri parsed = Uri.parse(uri);

        if (!"otpauth".equals(parsed.getScheme())) {
            throw new Exception("Invalid scheme: " + parsed.getScheme());
        }

        OTPEntry entry = new OTPEntry();

        // ── Type ──────────────────────────────────────────────
        String host = parsed.getHost();
        if ("totp".equalsIgnoreCase(host)) {
            entry.setType(OTPType.TOTP);
        } else if ("hotp".equalsIgnoreCase(host)) {
            entry.setType(OTPType.HOTP);
        } else if ("steam".equalsIgnoreCase(host)) {
            entry.setType(OTPType.STEAM);
        } else {
            throw new Exception("Unknown type: " + host);
        }

        // ── Name & Issuer from path ───────────────────────────
        String path = parsed.getPath();
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path != null && path.contains(":")) {
            String[] parts = path.split(":", 2);
            entry.setIssuer(Uri.decode(parts[0]));
            entry.setName(Uri.decode(parts[1]));
        } else if (path != null) {
            entry.setName(Uri.decode(path));
        }

        // ── Secret ────────────────────────────────────────────
        String secret = parsed.getQueryParameter("secret");
        if (secret == null || secret.isEmpty()) {
            throw new Exception("Missing secret");
        }
        entry.setSecret(secret.toUpperCase().replaceAll("\\s", ""));

        // ── Issuer override ───────────────────────────────────
        String issuer = parsed.getQueryParameter("issuer");
        if (issuer != null && !issuer.isEmpty()) {
            entry.setIssuer(issuer);
        }

        // ── Algorithm ─────────────────────────────────────────
        String algo = parsed.getQueryParameter("algorithm");
        if (algo != null) {
            switch (algo.toUpperCase()) {
                case "SHA256":
                    entry.setAlgorithm(OTPAlgorithm.SHA256); break;
                case "SHA512":
                    entry.setAlgorithm(OTPAlgorithm.SHA512); break;
                default:
                    entry.setAlgorithm(OTPAlgorithm.SHA1);   break;
            }
        }

        // ── Digits ────────────────────────────────────────────
        String digits = parsed.getQueryParameter("digits");
        if (digits != null) {
            entry.setDigits(Integer.parseInt(digits));
        }

        // ── Period ────────────────────────────────────────────
        String period = parsed.getQueryParameter("period");
        if (period != null) {
            entry.setPeriod(Long.parseLong(period));
        }

        // ── Counter ───────────────────────────────────────────
        String counter = parsed.getQueryParameter("counter");
        if (counter != null) {
            entry.setCounter(Long.parseLong(counter));
        }

        return entry;
    }

    public static boolean isValidOtpUri(String uri) {
        try {
            parse(uri);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}