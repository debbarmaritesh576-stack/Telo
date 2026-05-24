package com.telo.app.notifications;

import android.content.Context;
import com.telo.app.TeloApp;

public class BreachNotifier {

    private static final int NOTIF_ID = 3002;

    public static void notify(
            Context context, String title, int count) {
        NotificationHelper.showSimple(
            context,
            NOTIF_ID,
            TeloApp.CHANNEL_ALERT,
            "⚠️ Data Breach Detected!",
            "\"" + title + "\" found in " + count + " breach(es)"
        );
    }

    public static void cancel(Context context) {
        NotificationHelper.cancel(context, NOTIF_ID);
    }
}