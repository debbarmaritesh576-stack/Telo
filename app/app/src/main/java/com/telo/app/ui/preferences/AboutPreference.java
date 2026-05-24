package com.telo.app.ui.preferences;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import androidx.preference.Preference;

public class AboutPreference extends Preference {

    private static final String GITHUB_URL =
        "https://github.com/yourusername/telo";
    private static final String PRIVACY_URL =
        "https://teloapp.com/privacy";

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            String version = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .versionName;
            setSummary("Version " + version + " • Open Source");
        } catch (Exception e) {
            setSummary("Open Source");
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        openUrl(GITHUB_URL);
    }

    private void openUrl(String url) {
        Intent intent = new Intent(
            Intent.ACTION_VIEW, Uri.parse(url)
        );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }
}