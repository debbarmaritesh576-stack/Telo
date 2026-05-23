package com.telo.app.passwords;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BreachChecker {

    private static final String API_URL =
        "https://api.pwnedpasswords.com/range/";

    public interface Callback {
        void onResult(boolean isBreached, int count);
        void onError(Exception e);
    }

    // ── k-Anonymity Check (safe — only first 5 chars of hash sent) ───

    public static void check(String password, Callback callback) {
        new Thread(() -> {
            try {
                String hash   = sha1(password).toUpperCase();
                String prefix = hash.substring(0, 5);
                String suffix = hash.substring(5);

                URL               url  = new URL(API_URL + prefix);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("Add-Padding", "true");

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    callback.onError(new Exception("API error: " + responseCode));
                    return;
                }

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );

                String line;
                int    count = 0;
                boolean found = false;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2 &&
                        parts[0].equalsIgnoreCase(suffix)) {
                        count = Integer.parseInt(parts[1].trim());
                        found = true;
                        break;
                    }
                }
                reader.close();

                callback.onResult(found, count);

            } catch (Exception e) {
                callback.onError(e);
            }
        }).start();
    }

    // ── SHA1 ──────────────────────────────────────────────────

    private static String sha1(String input) throws Exception {
        MessageDigest md     = MessageDigest.getInstance("SHA-1");
        byte[]        bytes  = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb     = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}