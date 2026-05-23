package com.telo.app.passwords;

import java.util.List;
import java.util.stream.Collectors;

public class PasswordSearchHelper {

    public static List<PasswordEntry> search(
            List<PasswordEntry> entries, String query) {
        if (query == null || query.trim().isEmpty()) return entries;
        String q = query.toLowerCase().trim();
        return entries.stream()
            .filter(e ->
                contains(e.getTitle(),    q) ||
                contains(e.getUsername(), q) ||
                contains(e.getEmail(),    q) ||
                contains(e.getUrl(),      q)
            )
            .collect(Collectors.toList());
    }

    public static List<PasswordEntry> filterByCategory(
            List<PasswordEntry> entries, String categoryId) {
        if (categoryId == null || categoryId.equals("all")) return entries;
        return entries.stream()
            .filter(e -> categoryId.equals(e.getCategoryId()))
            .collect(Collectors.toList());
    }

    public static List<PasswordEntry> filterFavorites(
            List<PasswordEntry> entries) {
        return entries.stream()
            .filter(PasswordEntry::isFavorite)
            .collect(Collectors.toList());
    }

    private static boolean contains(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }
}