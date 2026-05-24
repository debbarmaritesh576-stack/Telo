package com.telo.app.security;

import com.telo.app.passwords.PasswordEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateFinder {

    public static class DuplicateGroup {
        public final String              password;
        public final List<PasswordEntry> entries;

        public DuplicateGroup(
                String password,
                List<PasswordEntry> entries) {
            this.password = password;
            this.entries  = entries;
        }
    }

    // ── Find Duplicate Passwords ──────────────────────────────

    public static List<DuplicateGroup> findDuplicatePasswords(
            List<PasswordEntry> entries) {
        Map<String, List<PasswordEntry>> map = new HashMap<>();

        for (PasswordEntry entry : entries) {
            if (entry.getPassword() == null ||
                entry.getPassword().isEmpty()) continue;

            map.computeIfAbsent(
                entry.getPassword(), k -> new ArrayList<>()
            ).add(entry);
        }

        List<DuplicateGroup> groups = new ArrayList<>();
        for (Map.Entry<String, List<PasswordEntry>> e
                : map.entrySet()) {
            if (e.getValue().size() > 1) {
                groups.add(new DuplicateGroup(
                    e.getKey(), e.getValue()
                ));
            }
        }
        return groups;
    }

    // ── Find Duplicate Usernames ──────────────────────────────

    public static List<List<PasswordEntry>> findDuplicateUsernames(
            List<PasswordEntry> entries) {
        Map<String, List<PasswordEntry>> map = new HashMap<>();

        for (PasswordEntry entry : entries) {
            if (entry.getUsername() == null ||
                entry.getUsername().isEmpty()) continue;

            map.computeIfAbsent(
                entry.getUsername().toLowerCase(),
                k -> new ArrayList<>()
            ).add(entry);
        }

        List<List<PasswordEntry>> duplicates = new ArrayList<>();
        for (List<PasswordEntry> group : map.values()) {
            if (group.size() > 1) duplicates.add(group);
        }
        return duplicates;
    }

    // ── Count ─────────────────────────────────────────────────

    public static int getDuplicateCount(
            List<PasswordEntry> entries) {
        return findDuplicatePasswords(entries).size();
    }
}