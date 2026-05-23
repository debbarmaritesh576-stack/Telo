package com.telo.app.crypto;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class SecureClipboard {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static Runnable clearRunnable;

    // ── Copy & Auto Clear ─────────────────────────────────────

    public static void copy(Context context, String label, String text) {
        ClipboardManager cm = (ClipboardManager) context
            .getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm == null) return;

        ClipData clip = ClipData.newPlainText(label, text);
        cm.setPrimaryClip(clip);

        scheduleClear(context);
    }

    // ── Schedule Auto Clear ───────────────────────────────────

    private static void scheduleClear(Context context) {
        if (clearRunnable != null) {
            HANDLER.removeCallbacks(clearRunnable);
        }
        clearRunnable = () -> clear(context);
        HANDLER.postDelayed(
            clearRunnable,
            CryptoConstants.CLIPBOARD_CLEAR_DELAY_MS
        );
    }

    // ── Clear ─────────────────────────────────────────────────

    public static void clear(Context context) {
        ClipboardManager cm = (ClipboardManager) context
            .getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm == null) return;

        ClipData clip = ClipData.newPlainText("", "");
        cm.setPrimaryClip(clip);

        if (clearRunnable != null) {
            HANDLER.removeCallbacks(clearRunnable);
            clearRunnable = null;
        }
    }

    public static void cancelAutoClear() {
        if (clearRunnable != null) {
            HANDLER.removeCallbacks(clearRunnable);
            clearRunnable = null;
        }
    }
}