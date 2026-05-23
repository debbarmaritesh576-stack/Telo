package com.telo.app.passwords;

public class PasswordStrengthChecker {

    public enum Strength {
        VERY_WEAK,
        WEAK,
        FAIR,
        STRONG,
        VERY_STRONG
    }

    public static class Result {
        public final Strength strength;
        public final int      score;
        public final String   feedback;

        public Result(Strength strength, int score, String feedback) {
            this.strength = strength;
            this.score    = score;
            this.feedback = feedback;
        }
    }

    public static Result check(String password) {
        if (password == null || password.isEmpty()) {
            return new Result(Strength.VERY_WEAK, 0, "Password is empty");
        }

        int score = 0;

        // Length
        if (password.length() >= 8)  score += 10;
        if (password.length() >= 12) score += 10;
        if (password.length() >= 16) score += 10;
        if (password.length() >= 20) score += 10;

        // Character types
        if (password.matches(".*[a-z].*"))       score += 10;
        if (password.matches(".*[A-Z].*"))       score += 10;
        if (password.matches(".*[0-9].*"))       score += 10;
        if (password.matches(".*[^a-zA-Z0-9].*")) score += 20;

        // Variety bonus
        long uniqueChars = password.chars().distinct().count();
        if (uniqueChars > 8)  score += 5;
        if (uniqueChars > 12) score += 5;

        // Penalties
        if (password.matches(".*(..)\\1.*"))     score -= 10; // repeated patterns
        if (isCommon(password))                  score -= 30;

        score = Math.max(0, Math.min(100, score));

        Strength strength;
        String   feedback;

        if (score < 20) {
            strength = Strength.VERY_WEAK;
            feedback = "Very weak — add more characters";
        } else if (score < 40) {
            strength = Strength.WEAK;
            feedback = "Weak — add uppercase, numbers & symbols";
        } else if (score < 60) {
            strength = Strength.FAIR;
            feedback = "Fair — consider making it longer";
        } else if (score < 80) {
            strength = Strength.STRONG;
            feedback = "Strong password";
        } else {
            strength = Strength.VERY_STRONG;
            feedback = "Very strong password";
        }

        return new Result(strength, score, feedback);
    }

    private static boolean isCommon(String password) {
        String[] common = {
            "password", "123456", "qwerty", "abc123",
            "letmein", "monkey", "dragon", "master",
            "123456789", "password1"
        };
        String lower = password.toLowerCase();
        for (String c : common) {
            if (lower.equals(c)) return true;
        }
        return false;
    }
}