package com.telo.app.passwords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordTagManager {

    private static final String SEPARATOR = ",";

    public static List<String> parseTags(String raw) {
        if (raw == null || raw.trim().isEmpty()) return new ArrayList<>();
        return Arrays.stream(raw.split(SEPARATOR))
            .map(String::trim)
            .filter(t -> !t.isEmpty())
            .collect(Collectors.toList());
    }

    public static String joinTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return "";
        return String.join(SEPARATOR, tags);
    }

    public static List<String> addTag(String raw, String tag) {
        List<String> tags = new ArrayList<>(parseTags(raw));
        if (!tags.contains(tag.trim())) {
            tags.add(tag.trim());
        }
        return tags;
    }

    public static List<String> removeTag(String raw, String tag) {
        return parseTags(raw).stream()
            .filter(t -> !t.equalsIgnoreCase(tag))
            .collect(Collectors.toList());
    }

    public static boolean hasTag(String raw, String tag) {
        return parseTags(raw).stream()
            .anyMatch(t -> t.equalsIgnoreCase(tag));
    }
}