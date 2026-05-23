package com.telo.app.autofill;

import android.app.Application;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordRepository;
import java.util.ArrayList;
import java.util.List;

public class AutofillRepository {

    private final PasswordRepository passwordRepository;
    private List<PasswordEntry> cachedEntries = new ArrayList<>();

    public AutofillRepository(Application app) {
        passwordRepository = new PasswordRepository(app);
    }

    public void loadEntries(Runnable onLoaded) {
        passwordRepository.getAll().observeForever(entries -> {
            if (entries != null) {
                cachedEntries = entries;
                if (onLoaded != null) onLoaded.run();
            }
        });
    }

    public List<PasswordEntry> findMatches(
            String packageName, String webDomain) {
        return AppMatcher.match(cachedEntries, packageName, webDomain);
    }

    public List<PasswordEntry> getAllEntries() {
        return new ArrayList<>(cachedEntries);
    }
}