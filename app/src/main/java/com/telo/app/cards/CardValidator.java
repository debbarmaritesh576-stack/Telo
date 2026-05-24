package com.telo.app.cards;

public class CardValidator {

    // ── Luhn Algorithm ────────────────────────────────────────

    public static boolean isValidNumber(String number) {
        if (number == null) return false;
        String digits = number.replaceAll("[^0-9]", "");
        if (digits.length() < 13 || digits.length() > 19) {
            return false;
        }
        return luhnCheck(digits);
    }

    private static boolean luhnCheck(String digits) {
        int sum     = 0;
        boolean alt = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(
                String.valueOf(digits.charAt(i))
            );
            if (alt) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alt  = !alt;
        }
        return sum % 10 == 0;
    }

    // ── Expiry ────────────────────────────────────────────────

    public static boolean isValidExpiry(
            String month, String year) {
        try {
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);
            if (m < 1 || m > 12) return false;
            if (y < 0 || y > 99) return false;

            java.util.Calendar now = java.util.Calendar.getInstance();
            int currentYear  = now.get(java.util.Calendar.YEAR) % 100;
            int currentMonth = now.get(java.util.Calendar.MONTH) + 1;

            if (y < currentYear) return false;
            if (y == currentYear && m < currentMonth) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ── CVV ───────────────────────────────────────────────────

    public static boolean isValidCvv(
            String cvv, CardEntry.CardType type) {
        if (cvv == null) return false;
        String digits = cvv.replaceAll("[^0-9]", "");
        if (type == CardEntry.CardType.AMEX) {
            return digits.length() == 4;
        }
        return digits.length() == 3;
    }

    // ── Cardholder ────────────────────────────────────────────

    public static boolean isValidCardholder(String name) {
        return name != null &&
               name.trim().length() >= 2 &&
               name.trim().length() <= 50;
    }
}