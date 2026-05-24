package com.telo.app.ui.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import androidx.preference.SwitchPreferenceCompat;

public class NotificationPreference extends SwitchPreferenceCompat {

    public NotificationPreference(
            Context context, AttributeSet attrs) {
        super(context, attrs);
        setTitle("Notifications");
        setSummary("Breach alerts & backup reminders");
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (!isChecked()) {
            // Open notification settings
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent = new Intent(
                    Settings.ACTION_APP_NOTIFICATION_SETTINGS
                );
                intent.putExtra(
                    Settings.EXTRA_APP_PACKAGE,
                    getContext().getPackageName()
                );
            } else {
                intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" +
                        getContext().getPackageName())
                );
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }
}