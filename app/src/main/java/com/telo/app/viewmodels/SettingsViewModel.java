package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.crypto.PinManager;
import com.telo.app.security.AutoLockManager;
import com.telo.app.util.PreferenceHelper;

public class SettingsViewModel extends AndroidViewModel {

    private final PinManager                     pinManager;
    private final AutoLockManager                autoLockManager;
    private final MutableLiveData<String>        errorLiveData;
    private final MutableLiveData<String>        successLiveData;
    private final MutableLiveData<Boolean>       biometricEnabled;
    private final MutableLiveData<Boolean>       screenshotBlocked;
    private final MutableLiveData<Boolean>       tapToReveal;
    private final MutableLiveData<Boolean>       autoBackup;
    private final MutableLiveData<String>        theme;
    private final MutableLiveData<AutoLockManager.LockTimeout> lockTimeout;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        pinManager        = new PinManager(application);
        autoLockManager   = AutoLockManager.getInstance(application);
        errorLiveData     = new MutableLiveData<>();
        successLiveData   = new MutableLiveData<>();
        biometricEnabled  = new MutableLiveData<>(
            PreferenceHelper.isBiometricEnabled());
        screenshotBlocked = new MutableLiveData<>(
            PreferenceHelper.isScreenshotBlocked());
        tapToReveal       = new MutableLiveData<>(
            PreferenceHelper.isTapToReveal());
        autoBackup        = new MutableLiveData<>(
            PreferenceHelper.isAutoBackupEnabled());
        theme             = new MutableLiveData<>(
            PreferenceHelper.getTheme());
        lockTimeout       = new MutableLiveData<>(
            autoLockManager.getLockTimeout());
    }

    // ── PIN ───────────────────────────────────────────────────

    public void setPin(String pin) {
        try {
            pinManager.setPin(pin);
            successLiveData.setValue("PIN set successfully");
        } catch (Exception e) {
            errorLiveData.setValue("PIN error: " + e.getMessage());
        }
    }

    public void removePin() {
        pinManager.removePin();
        successLiveData.setValue("PIN removed");
    }

    public boolean isPinEnabled() {
        return pinManager.isPinEnabled();
    }

    // ── Security ──────────────────────────────────────────────

    public void setBiometricEnabled(boolean enabled) {
        PreferenceHelper.setBiometricEnabled(enabled);
        biometricEnabled.setValue(enabled);
    }

    public void setScreenshotBlocked(boolean blocked) {
        PreferenceHelper.setScreenshotBlocked(blocked);
        screenshotBlocked.setValue(blocked);
    }

    public void setLockTimeout(AutoLockManager.LockTimeout timeout) {
        autoLockManager.setLockTimeout(timeout);
        lockTimeout.setValue(timeout);
    }

    // ── Display ───────────────────────────────────────────────

    public void setTapToReveal(boolean enabled) {
        PreferenceHelper.setTapToReveal(enabled);
        tapToReveal.setValue(enabled);
    }

    public void setTheme(String themeName) {
        PreferenceHelper.setTheme(themeName);
        theme.setValue(themeName);
    }

    // ── Backup ────────────────────────────────────────────────

    public void setAutoBackup(boolean enabled) {
        PreferenceHelper.setAutoBackupEnabled(enabled);
        autoBackup.setValue(enabled);
    }

    // ── LiveData ──────────────────────────────────────────────

    public LiveData<String>  getError()            { return errorLiveData; }
    public LiveData<String>  getSuccess()          { return successLiveData; }
    public LiveData<Boolean> getBiometricEnabled() { return biometricEnabled; }
    public LiveData<Boolean> getScreenshotBlocked(){ return screenshotBlocked; }
    public LiveData<Boolean> getTapToReveal()      { return tapToReveal; }
    public LiveData<Boolean> getAutoBackup()       { return autoBackup; }
    public LiveData<String>  getTheme()            { return theme; }
    public LiveData<AutoLockManager.LockTimeout> getLockTimeout() {
        return lockTimeout;
    }
}