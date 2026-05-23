package com.telo.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.crypto.PinManager;
import com.telo.app.vault.VaultManager;

public class UnlockViewModel extends AndroidViewModel {

    public enum UnlockState {
        IDLE,
        UNLOCKING,
        SUCCESS,
        FAILED,
        LOCKED_OUT
    }

    private final VaultManager              vaultManager;
    private final PinManager                pinManager;
    private final MutableLiveData<UnlockState> stateLiveData;
    private final MutableLiveData<String>   errorLiveData;
    private final MutableLiveData<Integer>  attemptsLiveData;
    private final MutableLiveData<Long>     lockoutTimeLiveData;

    private int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 5;

    public UnlockViewModel(@NonNull Application application) {
        super(application);
        vaultManager        = VaultManager.getInstance(application);
        pinManager          = new PinManager(application);
        stateLiveData       = new MutableLiveData<>(UnlockState.IDLE);
        errorLiveData       = new MutableLiveData<>();
        attemptsLiveData    = new MutableLiveData<>(0);
        lockoutTimeLiveData = new MutableLiveData<>(0L);
    }

    // ── Password Unlock ───────────────────────────────────────

    public void unlockWithPassword(char[] password) {
        stateLiveData.setValue(UnlockState.UNLOCKING);
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                boolean success = vaultManager.unlockWithPassword(password);
                if (success) {
                    failedAttempts = 0;
                    stateLiveData.postValue(UnlockState.SUCCESS);
                } else {
                    onFailed("Wrong password");
                }
            } catch (Exception e) {
                onFailed("Unlock error: " + e.getMessage());
            }
        });
    }

    // ── PIN Unlock ────────────────────────────────────────────

    public void unlockWithPin(String pin) {
        stateLiveData.setValue(UnlockState.UNLOCKING);
        try {
            boolean success = pinManager.verifyPin(pin);
            if (success) {
                failedAttempts = 0;
                stateLiveData.setValue(UnlockState.SUCCESS);
            } else {
                onFailed("Wrong PIN");
            }
        } catch (Exception e) {
            onFailed("PIN error: " + e.getMessage());
        }
    }

    // ── Biometric Unlock ──────────────────────────────────────

    public void onBiometricSuccess() {
        failedAttempts = 0;
        stateLiveData.setValue(UnlockState.SUCCESS);
    }

    public void onBiometricFailed() {
        onFailed("Biometric failed");
    }

    // ── State ─────────────────────────────────────────────────

    private void onFailed(String error) {
        failedAttempts++;
        attemptsLiveData.postValue(failedAttempts);

        if (failedAttempts >= MAX_ATTEMPTS) {
            stateLiveData.postValue(UnlockState.LOCKED_OUT);
            lockoutTimeLiveData.postValue(30_000L);
        } else {
            errorLiveData.postValue(error);
            stateLiveData.postValue(UnlockState.FAILED);
        }
    }

    public boolean isPinEnabled() {
        return pinManager.isPinEnabled();
    }

    public LiveData<UnlockState> getState()       { return stateLiveData; }
    public LiveData<String>      getError()        { return errorLiveData; }
    public LiveData<Integer>     getAttempts()     { return attemptsLiveData; }
    public LiveData<Long>        getLockoutTime()  { return lockoutTimeLiveData; }
}