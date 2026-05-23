package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.OTPEntryDao;
import com.telo.app.db.OTPEntryEntity;
import com.telo.app.otp.Base32;
import com.telo.app.otp.OTPAlgorithm;
import com.telo.app.otp.OTPEntry;
import com.telo.app.otp.OTPType;
import com.telo.app.otp.TOTPGenerator;

public class EditOTPViewModel extends AndroidViewModel {

    private final OTPEntryDao             dao;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<String> codeLiveData;
    private       OTPEntry                currentEntry;

    public EditOTPViewModel(@NonNull Application application) {
        super(application);
        dao           = AppDatabase.getInstance(application).otpEntryDao();
        errorLiveData = new MutableLiveData<>();
        codeLiveData  = new MutableLiveData<>();
        currentEntry  = new OTPEntry();
    }

    // ── Entry Setup ───────────────────────────────────────────

    public void loadEntry(String id) {
        AppDatabase.DB_EXECUTOR.execute(() -> {
            OTPEntryEntity entity = dao.getById(id);
            if (entity != null) {
                try {
                    currentEntry = entity.toOTPEntry();
                } catch (Exception e) {
                    errorLiveData.postValue(
                        "Load failed: " + e.getMessage()
                    );
                }
            }
        });
    }

    public void setFromQR(OTPEntry entry) {
        this.currentEntry = entry;
    }

    // ── Field Updates ─────────────────────────────────────────

    public void setName(String name)       { currentEntry.setName(name); }
    public void setIssuer(String issuer)   { currentEntry.setIssuer(issuer); }
    public void setSecret(String secret)   { currentEntry.setSecret(secret.toUpperCase().trim()); }
    public void setDigits(int digits)      { currentEntry.setDigits(digits); }
    public void setPeriod(long period)     { currentEntry.setPeriod(period); }
    public void setType(OTPType type)      { currentEntry.setType(type); }
    public void setAlgorithm(OTPAlgorithm algo) { currentEntry.setAlgorithm(algo); }

    // ── Validate & Save ───────────────────────────────────────

    public boolean validate() {
        if (currentEntry.getName() == null ||
            currentEntry.getName().isEmpty()) {
            errorLiveData.setValue("Name is required");
            return false;
        }
        if (currentEntry.getSecret() == null ||
            currentEntry.getSecret().isEmpty()) {
            errorLiveData.setValue("Secret is required");
            return false;
        }
        if (!Base32.isValid(currentEntry.getSecret())) {
            errorLiveData.setValue("Invalid Base32 secret");
            return false;
        }
        return true;
    }

    public void saveEntry() {
        if (!validate()) return;
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                OTPEntryEntity entity =
                    OTPEntryEntity.fromOTPEntry(currentEntry);
                dao.insert(entity);
            } catch (Exception e) {
                errorLiveData.postValue(
                    "Save failed: " + e.getMessage()
                );
            }
        });
    }

    // ── Preview Code ──────────────────────────────────────────

    public void generatePreviewCode() {
        try {
            String code = TOTPGenerator.generate(currentEntry);
            codeLiveData.setValue(code);
        } catch (Exception e) {
            codeLiveData.setValue("------");
        }
    }

    public OTPEntry getCurrentEntry()     { return currentEntry; }
    public LiveData<String> getError()    { return errorLiveData; }
    public LiveData<String> getCode()     { return codeLiveData; }
}