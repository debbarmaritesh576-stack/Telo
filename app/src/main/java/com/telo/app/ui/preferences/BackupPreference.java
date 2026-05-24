package com.telo.app.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.telo.app.util.PreferenceHelper;
import com.telo.app.util.TimeHelper;

public class BackupPreference extends Preference {

    public BackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateSummary();
    }

    private void updateSummary() {
        long lastBackup = PreferenceHelper.getLastBackupTime();
        if (lastBackup > 0) {
            setSummary(
                "Last backup: " + TimeHelper.formatRelative(lastBackup)
            );
        } else {
            setSummary("No backup yet — tap to backup now");
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        updateSummary();
    }
}