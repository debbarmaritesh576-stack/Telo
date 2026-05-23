package com.telo.app.passwords;

import java.util.ArrayList;
import java.util.List;

public class PasswordHistory {

    public static class HistoryEntry {
        public final String password;
        public final long   changedAt;

        public HistoryEntry(String password, long changedAt) {
            this.password  = password;
            this.changedAt = changedAt;
        }
    }

    private static final int MAX_HISTORY = 10;

    private final List<HistoryEntry> history = new ArrayList<>();

    public void add(String password) {
        history.add(0, new HistoryEntry(password, System.currentTimeMillis()));
        if (history.size() > MAX_HISTORY) {
            history.remove(history.size() - 1);
        }
    }

    public List<HistoryEntry> getAll() {
        return new ArrayList<>(history);
    }

    public void clear() {
        history.clear();
    }

    public int size() {
        return history.size();
    }
}