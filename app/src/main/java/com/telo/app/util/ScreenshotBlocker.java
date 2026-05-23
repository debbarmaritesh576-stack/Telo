package com.telo.app.util;

import android.app.Activity;
import android.view.WindowManager;

public class ScreenshotBlocker {

    public static void enable(Activity activity) {
        activity.getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }

    public static void disable(Activity activity) {
        activity.getWindow().clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        );
    }

    public static void apply(Activity activity, boolean block) {
        if (block) enable(activity);
        else        disable(activity);
    }
}