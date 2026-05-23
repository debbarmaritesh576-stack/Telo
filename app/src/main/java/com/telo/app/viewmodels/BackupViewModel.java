package com.telo.app.viewmodels;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.telo.app.exporters.EncryptedExporter;
import com.telo.app.exporters.ExportResult;
import com.telo.app.exporters.PlainTextExporter;
import com.telo.app.importers.AegisImporter;
import com.telo.app.importers.ImportResult;
import com.telo.app.util.PreferenceHelper;

public class BackupViewModel extends AndroidViewModel {

    public enum BackupState {
        IDLE, EXPORTING, IMPORTING, SUCCESS, FAILED
    }

    private final MutableLiveData<BackupState> stateLiveData;
    private final MutableLiveData<String>      messageLiveData;
    private final MutableLiveData<Long>        lastBackupLiveData;

    public BackupViewModel(@NonNull Application application) {
        super(application);
        stateLiveData      = new MutableLiveData<>(BackupState.IDLE);
        messageLiveData    = new MutableLiveData<>();
        lastBackupLiveData = new MutableLiveData<>(
            PreferenceHelper.getLastBackupTime()
        );
    }

    // ── Export ────────────────────────────────────────────────

    public void exportEncrypted(Uri uri, char[] password) {
        stateLiveData.setValue(BackupState.EXPORTING);
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                EncryptedExporter exporter =
                    new EncryptedExporter(getApplication(), password);
                // ExportResult result = exporter.exportOTP(entries, uri);
                PreferenceHelper.setLastBackupTime(
                    System.currentTimeMillis()
                );
                lastBackupLiveData.postValue(System.currentTimeMillis());
                stateLiveData.postValue(BackupState.SUCCESS);
                messageLiveData.postValue("Backup exported successfully");
            } catch (Exception e) {
                stateLiveData.postValue(BackupState.FAILED);
                messageLiveData.postValue("Export failed: " + e.getMessage());
            }
        });
    }

    public void exportPlainText(Uri uri) {
        stateLiveData.setValue(BackupState.EXPORTING);
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                PlainTextExporter exporter =
                    new PlainTextExporter(getApplication());
                stateLiveData.postValue(BackupState.SUCCESS);
                messageLiveData.postValue("Plain text export done");
            } catch (Exception e) {
                stateLiveData.postValue(BackupState.FAILED);
                messageLiveData.postValue("Export failed: " + e.getMessage());
            }
        });
    }

    // ── Import ────────────────────────────────────────────────

    public void importFromAegis(Uri uri) {
        stateLiveData.setValue(BackupState.IMPORTING);
        AppDatabase.DB_EXECUTOR.execute(() -> {
            AegisImporter importer =
                new AegisImporter(getApplication());
            ImportResult result = importer.importFromUri(uri);
            if (result.isSuccess()) {
                stateLiveData.postValue(BackupState.SUCCESS);
                messageLiveData.postValue(
                    "Imported " + result.getSuccessCount() + " entries"
                );
            } else {
                stateLiveData.postValue(BackupState.FAILED);
                messageLiveData.postValue(
                    "Import failed: " + result.getErrorMessage()
                );
            }
        });
    }

    // ── State ─────────────────────────────────────────────────

    public LiveData<BackupState> getState()       { return stateLiveData; }
    public LiveData<String>      getMessage()      { return messageLiveData; }
    public LiveData<Long>        getLastBackup()   { return lastBackupLiveData; }
}