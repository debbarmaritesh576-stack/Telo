package com.telo.app.ui.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import androidx.preference.Preference;

public class AutofillPreference extends Preference {

    public AutofillPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateSummary(context);
    }

    private void updateSummary(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.view.autofill.AutofillManager afm =
                context.getSystemService(
                    android.view.autofill.AutofillManager.class
                );
            if (afm != null && afm.hasEnabledAutofillServices()) {
                setSummary("Autofill: Enabled for Telo");
            } else {
                setSummary("Tap to enable autofill service");
            }
        } else {
            setSummary("Autofill requires Android 8.0+");
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(
                Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE,
                Uri.parse("package:" +
                    getContext().getPackageName())
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }
}