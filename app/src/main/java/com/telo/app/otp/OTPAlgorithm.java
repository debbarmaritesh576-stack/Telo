package com.telo.app.otp;

public enum OTPAlgorithm {
    SHA1("HmacSHA1"),
    SHA256("HmacSHA256"),
    SHA512("HmacSHA512");

    private final String hmacName;

    OTPAlgorithm(String hmacName) {
        this.hmacName = hmacName;
    }

    public String getHmacName() {
        return hmacName;
    }
}