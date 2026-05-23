package com.telo.app.crypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

public class KeyDerivation {

    // ── PBKDF2 ────────────────────────────────────────────────

    public static byte[] deriveKey(
            char[] password,
            byte[] salt,
            int    iterations,
            int    keyLengthBits) throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
            password, salt, iterations, keyLengthBits
        );
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(
                CryptoConstants.PBKDF2_ALGORITHM
            );
            return factory.generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }

    public static byte[] deriveKeyDefault(
            char[] password, byte[] salt) throws Exception {
        return deriveKey(
            password,
            salt,
            CryptoConstants.PBKDF2_ITERATIONS,
            CryptoConstants.PBKDF2_KEY_LENGTH
        );
    }

    // ── Salt Generation ───────────────────────────────────────

    public static byte[] generateSalt() {
        byte[] salt = new byte[CryptoConstants.SALT_SIZE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // ── Key Derivation Params ─────────────────────────────────

    public static class Params {
        public final byte[] salt;
        public final int    iterations;
        public final int    keyLengthBits;

        public Params(byte[] salt, int iterations, int keyLengthBits) {
            this.salt          = salt;
            this.iterations    = iterations;
            this.keyLengthBits = keyLengthBits;
        }

        public static Params createDefault() {
            return new Params(
                generateSalt(),
                CryptoConstants.PBKDF2_ITERATIONS,
                CryptoConstants.PBKDF2_KEY_LENGTH
            );
        }
    }
}