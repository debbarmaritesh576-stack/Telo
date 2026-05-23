package com.telo.app.passwords;

import java.util.List;
import java.util.stream.Collectors;

public class PasswordFilter {

    public enum SortBy {
        NAME_ASC,
        NAME_DESC,
        CREATED_NEWEST,
        CREATED_OLDEST,
        UPDATED_NEWEST
    }

    public static List<PasswordEntry> sort(
            List<PasswordEntry> entries, SortBy sortBy) {
        switch (sortBy) {
            case NAME_ASC:
                return entries.stream()
                    .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                    .collect(Collectors.toList());
            case NAME_DESC:
                return entries.stream()
                    .sorted((a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()))
                    .collect(Collectors.toList());
            case CREATED_NEWEST:
                return entries.stream()
                    .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                    .collect(Collectors.toList());
            case CREATED_OLDEST:
                return entries.stream()
                    .sorted((a, b) -> Long.compare(a.getCreatedAt(), b.getCreatedAt()))
                    .collect(Collectors.toList());
            case UPDATED_NEWEST:
                return entries.stream()
                    .sorted((a, b) -> Long.compare(b.getUpdatedAt(), a.getUpdatedAt()))
                    .collect(Collectors.toList());
            default:
                return entries;
        }
    }
}