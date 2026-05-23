package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.OTPEntryDao;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.otp.OTPEntry;
import com.telo.app.otp.TOTPGenerator;
import java.util.ArrayList;
import java.util.List;

public class OTPViewModel extends AndroidViewModel {

    private final OTPEntryDao              dao;
    private final MutableLiveData<String>  errorLiveData;
    private final MutableLiveData<Boolean> loadingLiveData;

    public OTPViewModel(@NonNull Application application) {
        super(application);
        dao             = AppDatabase.getInstance(application).otpEntryDao();
        errorLiveData   = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>(false);
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<OTPEntryEntity>> getAllEntries() {
        return dao.getAll();
    }

    public LiveData<List<OTPEntryEntity>> getByCategory(String categoryId) {
        return dao.getByCategory(categoryId);
    }

    public LiveData<List<OTPEntryEntity>> getFavorites() {
        return dao.getFavorites();
    }

    public LiveData<List<OTPEntryEntity>> search(String query) {
        return dao.search(query);
    }

    // ── Write ─────────────────────────────────────────────────

    public void addEntry(OTPEntry entry) {
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                OTPEntryEntity entity =
                    OTPEntryEntity.fromOTPEntry(entry);
                dao.insert(entity);
            } catch (Exception e) {
                errorLiveData.postValue(
                    "Failed to add: " + e.getMessage()
                );
            }
        });
    }

    public void deleteEntry(String id) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.deleteById(id)
        );
    }

    public void toggleFavorite(String id, boolean isFavorite) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.updateFavorite(id, isFavorite)
        );
    }

    public void incrementCounter(String id, long counter) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.updateCounter(id, counter + 1)
        );
    }

    // ── OTP Code ──────────────────────────────────────────────

    public String generateCode(OTPEntry entry) {
        try {
            return TOTPGenerator.generate(entry);
        } catch (Exception e) {
            return "------";
        }
    }

    public long getRemainingSeconds(OTPEntry entry) {
        return TOTPGenerator.getRemainingSeconds(entry);
    }

    // ── State ─────────────────────────────────────────────────

    public LiveData<String>  getError()   { return errorLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
}