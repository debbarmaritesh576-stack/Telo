package com.telo.app.util;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import java.util.concurrent.Executor;

public class BiometricHelper {

    public interface AuthCallback {
        void onSuccess();
        void onError(String error);
        void onFailed();
    }

    // ── Availability ──────────────────────────────────────────

    public static boolean isAvailable(Context context) {
        BiometricManager bm = BiometricManager.from(context);
        int result = bm.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        );
        return result == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public static String getUnavailableReason(Context context) {
        BiometricManager bm = BiometricManager.from(context);
        switch (bm.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return "No biometric hardware";
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return "Biometric hardware unavailable";
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return "No biometrics enrolled";
            default:
                return "Biometric unavailable";
        }
    }

    // ── Authenticate ──────────────────────────────────────────

    public static void authenticate(
            FragmentActivity activity,
            String           title,
            String           subtitle,
            String           negativeText,
            AuthCallback     callback) {

        Executor executor = ContextCompat.getMainExecutor(activity);

        BiometricPrompt prompt = new BiometricPrompt(
            activity, executor,
            new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(
                        BiometricPrompt.AuthenticationResult result) {
                    callback.onSuccess();
                }

                @Override
                public void onAuthenticationError(
                        int errorCode, CharSequence errString) {
                    callback.onError(errString.toString());
                }

                @Override
                public void onAuthenticationFailed() {
                    callback.onFailed();
                }
            }
        );

        BiometricPrompt.PromptInfo info =
            new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText(negativeText)
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
                .build();

        prompt.authenticate(info);
    }
}