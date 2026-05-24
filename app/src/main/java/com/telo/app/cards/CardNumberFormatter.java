package com.telo.app.cards;

public class CardNumberFormatter {

    // ── Format ────────────────────────────────────────────────

    public static String format(String raw) {
        if (raw == null) return "";
        String digits = raw.replaceAll("[^0-9]", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(" ");
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }

    public static String formatAmex(String raw) {
        if (raw == null) return "";
        String digits = raw.replaceAll("[^0-9]", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i == 4 || i == 10) sb.append(" ");
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }

    // ── Mask ──────────────────────────────────────────────────

    public static String mask(String raw) {
        if (raw == null || raw.length() < 4) {
            return "**** **** **** ****";
        }
        String digits = raw.replaceAll("[^0-9]", "");
        String last4  = digits.substring(
            Math.max(0, digits.length() - 4)
        );
        return "**** **** **** " + last4;
    }

    // ── Strip ─────────────────────────────────────────────────

    public static String stripSpaces(String formatted) {
        if (formatted == null) return "";
        return formatted.replaceAll("\\s", "");
    }

    // ── Detect Type ───────────────────────────────────────────

    public static CardEntry.CardType detectType(String number) {
        if (number == null || number.isEmpty()) {
            return CardEntry.CardType.OTHER;
        }
        String digits = number.replaceAll("[^0-9]", "");

        if (digits.startsWith("4")) {
            return CardEntry.CardType.VISA;
        } else if (digits.startsWith("5") ||
                   digits.startsWith("2")) {
            return CardEntry.CardType.MASTERCARD;
        } else if (digits.startsWith("34") ||
                   digits.startsWith("37")) {
            return CardEntry.CardType.AMEX;
        } else if (digits.startsWith("6")) {
            return CardEntry.CardType.RUPAY;
        }
        return CardEntry.CardType.OTHER;
    }
}