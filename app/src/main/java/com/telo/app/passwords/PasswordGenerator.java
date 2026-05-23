package com.telo.app.passwords;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS    = "0123456789";
    private static final String SYMBOLS   = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String AMBIGUOUS = "Il1O0";

    private final SecureRandom random = new SecureRandom();

    private int     length          = 16;
    private boolean useLowercase    = true;
    private boolean useUppercase    = true;
    private boolean useDigits       = true;
    private boolean useSymbols      = true;
    private boolean excludeAmbiguous = false;

    // ── Builder Style ─────────────────────────────────────────

    public PasswordGenerator length(int length) {
        this.length = length;
        return this;
    }

    public PasswordGenerator useLowercase(boolean use) {
        this.useLowercase = use;
        return this;
    }

    public PasswordGenerator useUppercase(boolean use) {
        this.useUppercase = use;
        return this;
    }

    public PasswordGenerator useDigits(boolean use) {
        this.useDigits = use;
        return this;
    }

    public PasswordGenerator useSymbols(boolean use) {
        this.useSymbols = use;
        return this;
    }

    public PasswordGenerator excludeAmbiguous(boolean exclude) {
        this.excludeAmbiguous = exclude;
        return this;
    }

    // ── Generate ──────────────────────────────────────────────

    public String generate() {
        StringBuilder charset = new StringBuilder();
        if (useLowercase) charset.append(LOWERCASE);
        if (useUppercase) charset.append(UPPERCASE);
        if (useDigits)    charset.append(DIGITS);
        if (useSymbols)   charset.append(SYMBOLS);

        if (charset.length() == 0) charset.append(LOWERCASE);

        String chars = charset.toString();
        if (excludeAmbiguous) {
            for (char c : AMBIGUOUS.toCharArray()) {
                chars = chars.replace(String.valueOf(c), "");
            }
        }

        StringBuilder password = new StringBuilder();
        // Ensure at least one char from each selected set
        if (useLowercase) password.append(randomChar(LOWERCASE));
        if (useUppercase) password.append(randomChar(UPPERCASE));
        if (useDigits)    password.append(randomChar(DIGITS));
        if (useSymbols)   password.append(randomChar(SYMBOLS));

        while (password.length() < length) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return shuffle(password.toString());
    }

    // ── Passphrase ────────────────────────────────────────────

    public String generatePassphrase(int wordCount, String separator) {
        String[] words = {
            "apple", "brave", "cloud", "dream", "eagle",
            "flame", "grace", "happy", "ivory", "jewel",
            "karma", "lemon", "magic", "noble", "ocean",
            "pearl", "quest", "river", "stone", "tiger",
            "ultra", "vivid", "water", "xenon", "youth", "zebra"
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            if (i > 0) sb.append(separator);
            sb.append(words[random.nextInt(words.length)]);
        }
        return sb.toString();
    }

    // ── Helpers ───────────────────────────────────────────────

    private char randomChar(String charset) {
        return charset.charAt(random.nextInt(charset.length()));
    }

    private String shuffle(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
    }
}