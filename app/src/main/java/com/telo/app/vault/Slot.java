package com.telo.app.vault;

public abstract class Slot {

    public enum Type {
        RAW(0),
        PASSWORD(1),
        BIOMETRIC(2);

        private final int value;

        Type(int value) { this.value = value; }

        public int getValue() { return value; }

        public static Type fromValue(int value) {
            for (Type t : values()) {
                if (t.value == value) return t;
            }
            throw new IllegalArgumentException("Unknown slot type: " + value);
        }
    }

    private String id;
    private Type   type;

    // Encrypted master key stored in this slot
    private byte[] encryptedMasterKey;
    private byte[] nonce;

    public Slot(Type type) {
        this.type = type;
    }

    public String getId()                   { return id; }
    public Type getType()                   { return type; }
    public byte[] getEncryptedMasterKey()   { return encryptedMasterKey; }
    public byte[] getNonce()                { return nonce; }

    public void setId(String id)                          { this.id = id; }
    public void setEncryptedMasterKey(byte[] key)         { this.encryptedMasterKey = key; }
    public void setNonce(byte[] nonce)                    { this.nonce = nonce; }
}