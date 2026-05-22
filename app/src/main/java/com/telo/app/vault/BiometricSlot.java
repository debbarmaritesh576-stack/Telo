package com.telo.app.vault;

public class BiometricSlot extends Slot {

    private String keyAlias;

    public BiometricSlot() {
        super(Type.BIOMETRIC);
        this.keyAlias = "telo_biometric_key";
    }

    public String getKeyAlias()           { return keyAlias; }
    public void setKeyAlias(String alias) { this.keyAlias = alias; }
}