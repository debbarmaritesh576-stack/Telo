package com.telo.app.otp;

public class Base32 {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static byte[] decode(String input) {
        if (input == null || input.isEmpty()) return new byte[0];
        input = input.toUpperCase()
                     .replaceAll("\\s", "")
                     .replaceAll("=", "");

        int outputLen = (input.length() * 5) / 8;
        byte[] output = new byte[outputLen];
        int buffer = 0, bitsLeft = 0, index = 0;

        for (char c : input.toCharArray()) {
            int val = ALPHABET.indexOf(c);
            if (val < 0) continue;
            buffer   = (buffer << 5) | val;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                output[index++] = (byte) (buffer >> (bitsLeft - 8));
                bitsLeft -= 8;
            }
        }
        return output;
    }

    public static String encode(byte[] input) {
        if (input == null || input.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        int buffer = input[0], next = 1, bitsLeft = 8;

        while (bitsLeft > 0 || next < input.length) {
            if (bitsLeft < 5) {
                if (next < input.length) {
                    buffer <<= 8;
                    buffer |= (input[next++] & 0xFF);
                    bitsLeft += 8;
                } else {
                    int pad = 5 - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }
            int idx = 0x1F & (buffer >> (bitsLeft - 5));
            bitsLeft -= 5;
            sb.append(ALPHABET.charAt(idx));
        }
        return sb.toString();
    }

    public static boolean isValid(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.toUpperCase()
                    .replaceAll("\\s", "")
                    .replaceAll("=", "")
                    .matches("[A-Z2-7]+");
    }
}