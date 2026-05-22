package com.telo.app.otp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

public class SteamGenerator {

    private static final String STEAM_ALPHABET = "23456789BCDFGHJKMNPQRTVWXY";
    private static final int    STEAM_DIGITS   = 5;
    private static final long   STEAM_PERIOD   = 30;

    public static String generate(OTPEntry entry) throws Exception {
        byte[] secret    = Base32.decode(entry.getSecret());
        long   timeStep  = System.currentTimeMillis() / 1000 / STEAM_PERIOD;
        byte[] stepBytes = ByteBuffer.allocate(8).putLong(timeStep).array();

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(secret, "HmacSHA1"));
        byte[] hash = mac.doFinal(stepBytes);

        int offset = hash[hash.length - 1] & 0x0F;
        int code = ((hash[offset]     & 0x7F) << 24)
                 | ((hash[offset + 1] & 0xFF) << 16)
                 | ((hash[offset + 2] & 0xFF) << 8)
                 |  (hash[offset + 3] & 0xFF);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STEAM_DIGITS; i++) {
            sb.append(STEAM_ALPHABET.charAt(code % STEAM_ALPHABET.length()));
            code /= STEAM_ALPHABET.length();
        }
        return sb.toString();
    }

    public static long getRemainingSeconds() {
        long now = System.currentTimeMillis() / 1000;
        return STEAM_PERIOD - (now % STEAM_PERIOD);
    }
}