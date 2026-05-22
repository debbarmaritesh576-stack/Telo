package com.telo.app.otp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

public class TOTPGenerator {

    public static String generate(OTPEntry entry) throws Exception {
        long timeStep = System.currentTimeMillis() / 1000 / entry.getPeriod();
        return generateAtStep(entry, timeStep);
    }

    public static String generateAtStep(OTPEntry entry, long step) throws Exception {
        byte[] secret    = Base32.decode(entry.getSecret());
        byte[] stepBytes = ByteBuffer.allocate(8).putLong(step).array();

        String algo = entry.getAlgorithm().getHmacName();
        Mac mac     = Mac.getInstance(algo);
        mac.init(new SecretKeySpec(secret, algo));
        byte[] hash = mac.doFinal(stepBytes);

        int offset = hash[hash.length - 1] & 0x0F;
        int otp = ((hash[offset]     & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                |  (hash[offset + 3] & 0xFF);

        otp = otp % (int) Math.pow(10, entry.getDigits());
        return String.format("%0" + entry.getDigits() + "d", otp);
    }

    public static long getRemainingSeconds(OTPEntry entry) {
        long now = System.currentTimeMillis() / 1000;
        return entry.getPeriod() - (now % entry.getPeriod());
    }

    public static float getProgress(OTPEntry entry) {
        long now     = System.currentTimeMillis() / 1000;
        long elapsed = now % entry.getPeriod();
        return 1f - ((float) elapsed / entry.getPeriod());
    }
}