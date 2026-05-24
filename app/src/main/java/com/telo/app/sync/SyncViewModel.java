package com.telo.app.sync;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SyncViewModel extends AndroidViewModel {

    private final SyncRepository            repository;
    private final MutableLiveData<SyncStatus> statusLiveData;
    private final MutableLiveData<Boolean>  autoSyncLiveData;

    public SyncViewModel(@NonNull Application application) {
        super(application);
        repository       = new SyncRepository(application);
        statusLiveData   = new MutableLiveData<>(SyncStatus.idle());
        autoSyncLiveData = new MutableLiveData<>(
            repository.isAutoSyncEnabled()
        );
    }

    public void syncNow() {
        statusLiveData.setValue(SyncStatus.syncing());
        repository.syncNow(status ->
            statusLiveData.postValue(status)
        );
    }

    public void setAutoSync(boolean enabled) {
        if (enabled) {
            repository.enableAutoSync();
        } else {
            repository.disableAutoSync();
        }
        autoSyncLiveData.setValue(enabled);
    }

    public LiveData<SyncStatus> getStatus()    { return statusLiveData; }
    public LiveData<Boolean>    getAutoSync()   { return autoSyncLiveData; }
}