package com.telo.app.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.ListPreference;
import androidx.appcompat.app.AppCompatDelegate;

public class AppearancePreference extends ListPreference {

    public AppearancePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEntries(new String[]{
            "Light", "Dark", "AMOLED", "System Default"
        });
        setEntryValues(new String[]{
            "light", "dark", "amoled", "system"
        });
    }

    @Override
    protected void onClick() {
        super.onClick();
    }

    public static void applyTheme(String theme) {
        switch (theme) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                );
                break;
            case "dark":
            case "amoled":
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                );
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                );
                break;
        }
    }
}