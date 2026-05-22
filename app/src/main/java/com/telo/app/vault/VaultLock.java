package com.telo.app.vault;

public class VaultLock {

    public enum UnlockMethod {
        PASSWORD,
        BIOMETRIC,
        PIN
    }

    private boolean      isUnlocked;
    private UnlockMethod lastMethod;
    private long         unlockedAt;
    private int          failedAttempts;
    private long         lockoutUntil;

    private static final int  MAX_ATTEMPTS    = 5;
    private static final long LOCKOUT_MS      = 30_000; // 30 seconds

    public VaultLock() {
        this.isUnlocked     = false;
        this.failedAttempts = 0;
        this.lockoutUntil   = 0;
    }

    public boolean isUnlocked()       { return isUnlocked; }
    public UnlockMethod getLastMethod() { return lastMethod; }
    public long getUnlockedAt()       { return unlockedAt; }
    public int getFailedAttempts()    { return failedAttempts; }

    public boolean isLockedOut() {
        return System.currentTimeMillis() < lockoutUntil;
    }

    public long getLockoutRemainingMs() {
        return Math.max(0, lockoutUntil - System.currentTimeMillis());
    }

    public void onUnlockSuccess(UnlockMethod method) {
        this.isUnlocked     = true;
        this.lastMethod     = method;
        this.unlockedAt     = System.currentTimeMillis();
        this.failedAttempts = 0;
        this.lockoutUntil   = 0;
    }

    public void onUnlockFailed() {
        this.failedAttempts++;
        if (this.failedAttempts >= MAX_ATTEMPTS) {
            this.lockoutUntil = System.currentTimeMillis() + LOCKOUT_MS;
        }
    }

    public void lock() {
        this.isUnlocked = false;
    }

    public void reset() {
        this.isUnlocked     = false;
        this.failedAttempts = 0;
        this.lockoutUntil   = 0;
    }
}