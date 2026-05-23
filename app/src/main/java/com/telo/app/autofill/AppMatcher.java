package com.telo.app.autofill;

import com.telo.app.passwords.PasswordEntry;
import java.util.ArrayList;
import java.util.List;

public class AppMatcher {

    public static List<PasswordEntry> match(
            List<PasswordEntry> entries,
            String packageName,
            String webDomain) {

        List<PasswordEntry> matches = new ArrayList<>();

        for (PasswordEntry entry : entries) {
            if (matchesDomain(entry, webDomain) ||
                matchesPackage(entry, packageName)) {
                matches.add(entry);
            }
        }

        return matches;
    }

    private static boolean matchesDomain(PasswordEntry entry, String domain) {
        if (domain == null || entry.getUrl() == null) return false;
        String entryDomain = extractDomain(entry.getUrl());
        String cleanDomain = extractDomain(domain);
        return entryDomain != null &&
               cleanDomain != null &&
               entryDomain.equalsIgnoreCase(cleanDomain);
    }

    private static boolean matchesPackage(PasswordEntry entry, String pkg) {
        if (pkg == null || entry.getUrl() == null) return false;
        // Simple check — pkg name contains domain keyword
        String url = entry.getUrl().toLowerCase();
        String p   = pkg.toLowerCase();
        // e.g. com.google.android → google
        String[] parts = p.split("\\.");
        for (String part : parts) {
            if (part.length() > 3 && url.contains(part)) return true;
        }
        return false;
    }

    private static String extractDomain(String url) {
        if (url == null) return null;
        try {
            String cleaned = url
                .replace("https://", "")
                .replace("http://", "")
                .replace("www.", "");
            int slash = cleaned.indexOf('/');
            return slash >= 0 ? cleaned.substring(0, slash) : cleaned;
        } catch (Exception e) {
            return null;
        }
    }
}