package com.telo.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.telo.app.crypto.SecureClipboard;

public class ClipboardClearReceiver extends BroadcastReceiver {

    public static final String ACTION_CLEAR =
        "com.telo.app.action.CLEAR_CLIPBOARD";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_CLEAR.equals(intent.getAction())) {
            SecureClipboard.clear(context);
        }
    }
}