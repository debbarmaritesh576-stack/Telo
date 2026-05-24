package com.telo.app.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.telo.app.crypto.BiometricCipher;
import com.telo.app.crypto.PinManager;
import com.telo.app.util.PreferenceHelper;

public class SecurityPreference extends Preference {

    private final PinManager pinManager;

    public SecurityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        pinManager = new PinManager(context);
        updateSummary();
    }

    private void updateSummary() {
        StringBuilder sb = new StringBuilder();

        if (BiometricCipher.isBiometricAvailable(getContext()) &&
            PreferenceHelper.isBiometricEnabled()) {
            sb.append("Biometric: ON");
        } else {
            sb.append("Biometric: OFF");
        }

        sb.append(" • ");

        if (pinManager.isPinEnabled()) {
            sb.append("PIN: SET");
        } else {
            sb.append("PIN: NOT SET");
        }

        setSummary(sb.toString());
    }

    @Override
    protected void onClick() {
        super.onClick();
        updateSummary();
    }
}