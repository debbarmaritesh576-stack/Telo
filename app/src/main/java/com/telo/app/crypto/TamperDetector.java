  package com.telo.app.crypto;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.security.MessageDigest;

public class TamperDetector {

    // Store your release signing cert SHA256 here
    private static final String EXPECTED_SIGNATURE_SHA256 =
        "YOUR_RELEASE_CERT_SHA256_HERE";

    // ── Signature Check ───────────────────────────────────────

    public static boolean isSignatureValid(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                .getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES
                );

            for (Signature sig : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(sig.toByteArray());
                String actualSha256 = HashManager.bytesToHex(md.digest());

                if (!actualSha256.equals(EXPECTED_SIGNATURE_SHA256)) {
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // ── Debugger Check ────────────────────────────────────────

    public static boolean isDebuggerAttached() {
        return android.os.Debug.isDebuggerConnected();
    }

    // ── Emulator Check ────────────────────────────────────────

    public static boolean isEmulator() {
        return android.os.Build.FINGERPRINT.startsWith("generic")
            || android.os.Build.FINGERPRINT.startsWith("unknown")
            || android.os.Build.MODEL.contains("google_sdk")
            || android.os.Build.MODEL.contains("Emulator")
            || android.os.Build.MODEL.contains("Android SDK")
            || android.os.Build.MANUFACTURER.contains("Genymotion")
            || android.os.Build.BRAND.startsWith("generic")
            || android.os.Build.DEVICE.startsWith("generic");
    }

    // ── Full Check ────────────────────────────────────────────

    public static boolean isTampered(Context context) {
        return !isSignatureValid(context) || isDebuggerAttached();
    }
}