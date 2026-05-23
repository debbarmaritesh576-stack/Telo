package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordGenerator;
import com.telo.app.passwords.PasswordRepository;
import com.telo.app.passwords.PasswordStrengthChecker;

public class EditPasswordViewModel extends AndroidViewModel {

    private final PasswordRepository                    repository;
    private final MutableLiveData<String>               errorLiveData;
    private final MutableLiveData<PasswordStrengthChecker.Result> strengthLiveData;
    private       PasswordEntry                         currentEntry;

    public EditPasswordViewModel(@NonNull Application application) {
        super(application);
        repository       = new PasswordRepository(application);
        errorLiveData    = new MutableLiveData<>();
        strengthLiveData = new MutableLiveData<>();
        currentEntry     = new PasswordEntry();
    }

    // ── Load ──────────────────────────────────────────────────

    public void loadEntry(String id) {
        // Load from repository
        repository.getAll().observeForever(entries -> {
            if (entries != null) {
                for (PasswordEntry e : entries) {
                    if (e.getId().equals(id)) {
                        currentEntry = e;
                        break;
                    }
                }
            }
        });
    }

    // ── Field Updates ─────────────────────────────────────────

    public void setTitle(String title)       { currentEntry.setTitle(title); }
    public void setUsername(String username) { currentEntry.setUsername(username); }
    public void setEmail(String email)       { currentEntry.setEmail(email); }
    public void setUrl(String url)           { currentEntry.setUrl(url); }
    public void setNotes(String notes)       { currentEntry.setNotes(notes); }
    public void setCategoryId(String id)     { currentEntry.setCategoryId(id); }

    public void setPassword(String password) {
        currentEntry.setPassword(password);
        checkStrength(password);
    }

    // ── Strength ──────────────────────────────────────────────

    public void checkStrength(String password) {
        PasswordStrengthChecker.Result result =
            PasswordStrengthChecker.check(password);
        strengthLiveData.setValue(result);
    }

    // ── Generate ──────────────────────────────────────────────

    public String generatePassword(
            int length,
            boolean upper,
            boolean digits,
            boolean symbols) {
        String generated = new PasswordGenerator()
            .length(length)
            .useUppercase(upper)
            .useDigits(digits)
            .useSymbols(symbols)
            .generate();
        setPassword(generated);
        return generated;
    }

    // ── Validate & Save ───────────────────────────────────────

    public boolean validate() {
        if (currentEntry.getTitle() == null ||
            currentEntry.getTitle().isEmpty()) {
            errorLiveData.setValue("Title is required");
            return false;
        }
        if (currentEntry.getPassword() == null ||
            currentEntry.getPassword().isEmpty()) {
            errorLiveData.setValue("Password is required");
            return false;
        }
        return true;
    }

    public void saveEntry() {
        if (!validate()) return;
        if (currentEntry.getCreatedAt() == 0) {
            repository.insert(currentEntry);
        } else {
            repository.update(currentEntry);
        }
    }

    public void deleteEntry() {
        repository.delete(currentEntry.getId());
    }

    public PasswordEntry getCurrentEntry() { return currentEntry; }
    public LiveData<String> getError()     { return errorLiveData; }
    public LiveData<PasswordStrengthChecker.Result> getStrength() {
        return strengthLiveData;
    }
}