package com.telo.app.autofill;

import android.app.Application;
import androidx.lifecycle.Observer;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.PasswordEntryDao;
import com.telo.app.db.PasswordEntryEntity;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutofillRepository {

    private final PasswordEntryDao          dao;
    private final ExecutorService           executor;
    private       List<PasswordEntry>       cachedEntries;

    public AutofillRepository(Application app) {
        this.dao          = AppDatabase.getInstance(app).passwordEntryDao();
        this.executor     = Executors.newSingleThreadExecutor();
        this.cachedEntries = new ArrayList<>();
    }

    // ── No observeForever — direct DB query ───────────────────

    public void loadEntries(Runnable onLoaded) {
        executor.execute(() -> {
            try {
                List<PasswordEntryEntity> entities =
                    dao.getAllSync();  // synchronous query
                List<PasswordEntry> entries = new ArrayList<>();
                for (PasswordEntryEntity e : entities) {
                    entries.add(toEntry(e));
                }
                cachedEntries = entries;
                if (onLoaded != null) onLoaded.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public List<PasswordEntry> findMatches(
            String packageName, String webDomain) {
        return AppMatcher.match(cachedEntries, packageName, webDomain);
    }

    public void shutdown() {
        executor.shutdown();
    }

    private PasswordEntry toEntry(PasswordEntryEntity e) {
        PasswordEntry entry = new PasswordEntry();
        entry.setId(e.id);
        entry.setTitle(e.title);
        entry.setUsername(e.username);
        entry.setEmail(e.email);
        entry.setUrl(e.url);
        try {
            entry.setPassword(
                com.telo.app.crypto.CryptoManager
                    .decryptFromBase64(e.encryptedPassword)
            );
        } catch (Exception ex) {
            entry.setPassword("");
        }
        return entry;
    }
}