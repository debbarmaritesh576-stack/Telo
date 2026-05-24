package com.telo.app.sync;

public class SyncStatus {

    public enum State {
        IDLE,
        SYNCING,
        SUCCESS,
        FAILED,
        NO_NETWORK
    }

    private State  state;
    private String message;
    private long   lastSyncTime;
    private int    syncedCount;

    public SyncStatus() {
        this.state = State.IDLE;
    }

    public static SyncStatus idle() {
        SyncStatus s = new SyncStatus();
        s.state = State.IDLE;
        return s;
    }

    public static SyncStatus syncing() {
        SyncStatus s = new SyncStatus();
        s.state = State.SYNCING;
        return s;
    }

    public static SyncStatus success(int count) {
        SyncStatus s = new SyncStatus();
        s.state        = State.SUCCESS;
        s.syncedCount  = count;
        s.lastSyncTime = System.currentTimeMillis();
        s.message      = "Synced " + count + " items";
        return s;
    }

    public static SyncStatus failed(String error) {
        SyncStatus s = new SyncStatus();
        s.state   = State.FAILED;
        s.message = error;
        return s;
    }

    public State  getState()        { return state; }
    public String getMessage()      { return message; }
    public long   getLastSyncTime() { return lastSyncTime; }
    public int    getSyncedCount()  { return syncedCount; }
    public boolean isSuccess()      { return state == State.SUCCESS; }
    public boolean isSyncing()      { return state == State.SYNCING; }
}