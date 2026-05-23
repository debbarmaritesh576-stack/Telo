package com.telo.app.util;

import android.content.Context;
import com.telo.app.crypto.SecureClipboard;

public class ClipboardHelper {

    public static void copyOTPCode(Context context, String code) {
        SecureClipboard.copy(context, "OTP Code", code);
    }

    public static void copyPassword(Context context, String password) {
        SecureClipboard.copy(context, "Password", password);
    }

    public static void copyUsername(Context context, String username) {
        SecureClipboard.copy(context, "Username", username);
    }

    public static void copyUrl(Context context, String url) {
        SecureClipboard.copy(context, "URL", url);
    }

    public static void clear(Context context) {
        SecureClipboard.clear(context);
    }
}