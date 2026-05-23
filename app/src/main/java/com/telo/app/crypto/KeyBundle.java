package com.telo.app.crypto;

import java.util.Arrays;

public class KeyBundle {

    private final byte[] key;
    private final byte[] iv;
    private final byte[] salt;

    public KeyBundle(byte[] key, byte[] iv, byte[] salt) {
        this.key  = key;
        this.iv   = iv;
        this.salt = salt;
    }

    public static KeyBundle generate(char[] password) throws Exception {
        byte[] salt = KeyDerivation.generateSalt();
        byte[] key  = KeyDerivation.deriveKeyDefault(password, salt);
        byte[] iv   = new java.security.SecureRandom()
            .generateSeed(CryptoConstants.IV_SIZE);
        return new KeyBundle(key, iv, salt);
    }

    public byte[] getKey()  { return key; }
    public byte[] getIv()   { return iv; }
    public byte[] getSalt() { return salt; }

    public void wipe() {
        Arrays.fill(key,  (byte) 0);
        Arrays.fill(iv,   (byte) 0);
        Arrays.fill(salt, (byte) 0);
    }
}