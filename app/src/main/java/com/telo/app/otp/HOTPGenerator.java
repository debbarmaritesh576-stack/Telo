package com.telo.app.otp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

public class HOTPGenerator {

    public static String generate(OTPEntry entry) throws Exception {
        byte[] secret       = Base32.decode(entry.getSecret());
        byte[] counterBytes = ByteBuffer.allocate(8).putLong(entry.getCounter()).array();

        String algo = entry.getAlgorithm().getHmacName();
        Mac mac     = Mac.getInstance(algo);
        mac.init(new SecretKeySpec(secret, algo));
        byte[] hash = mac.doFinal(counterBytes);

        int offset = hash[hash.length - 1] & 0x0F;
        int otp = ((hash[offset]     & 0x7F) << 24)
                | ((hash[offset + 1] & 0xFF) << 16)
                | ((hash[offset + 2] & 0xFF) << 8)
                |  (hash[offset + 3] & 0xFF);

        otp = otp % (int) Math.pow(10, entry.getDigits());
        return String.format("%0" + entry.getDigits() + "d", otp);
    }
}