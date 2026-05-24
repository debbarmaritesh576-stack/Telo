package com.telo.app.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.telo.app.R;
import com.telo.app.util.TimeHelper;

public class TimeSyncDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(
            @Nullable Bundle savedInstanceState) {

        long currentTime = TimeHelper.getCurrentTimeSeconds();
        String timeStr   = TimeHelper.formatDateTime(
            currentTime * 1000
        );

        return new AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Time Sync Warning")
            .setMessage(
                "Your device time may be incorrect.\n\n" +
                "Current time: " + timeStr + "\n\n" +
                "Incorrect time will generate wrong OTP codes.\n" +
                "Please sync your device time in system settings."
            )
            .setPositiveButton("Open Settings", (d, w) -> {
                startActivity(new android.content.Intent(
                    android.provider.Settings.ACTION_DATE_SETTINGS
                ));
            })
            .setNegativeButton("Dismiss", null)
            .create();
    }
}