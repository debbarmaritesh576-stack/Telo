package com.telo.app.notifications;

import android.content.Context;
import com.telo.app.TeloApp;

public class ExpiryNotifier {

    private static final int NOTIF_ID = 3001;

    public static void notify(Context context, int count) {
        NotificationHelper.showSimple(
            context,
            NOTIF_ID,
            TeloApp.CHANNEL_ALERT,
            "⏰ Password Expiry Warning",
            count + " password(s) expiring soon — update them now"
        );
    }

    public static void cancel(Context context) {
        NotificationHelper.cancel(context, NOTIF_ID);
    }
}