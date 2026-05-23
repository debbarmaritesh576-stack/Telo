package com.telo.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.telo.app.services.AutoLockService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        // Start auto lock service on boot
        Intent serviceIntent = new Intent(context, AutoLockService.class);
        context.startService(serviceIntent);
    }
}