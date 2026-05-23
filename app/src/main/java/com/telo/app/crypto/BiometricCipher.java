package com.telo.app.crypto;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.concurrent.Executor;

public class BiometricCipher {

    public interface BiometricCallback {
        void onSuccess(Cipher cipher);
        void onError(String error);
        void onFailed();
    }

    // ── Check Availability ────────────────────────────────────

    public static boolean isBiometricAvailable(Context context) {
        BiometricManager bm = BiometricManager.from(context);
        return bm.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    // ── Encrypt Mode ──────────────────────────────────────────

    public static void authenticateForEncrypt(
            FragmentActivity activity,
            String           title,
            String           subtitle,
            BiometricCallback callback) {
        try {
            SecretKey key    = KeyStoreManager.getBiometricKey();
            Cipher    cipher = Cipher.getInstance(CryptoConstants.AES_TRANSFORM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            showPrompt(activity, title, subtitle, cipher, callback);

        } catch (Exception e) {
            callback.onError("Biometric setup failed: " + e.getMessage());
        }
    }

    // ── Decrypt Mode ──────────────────────────────────────────

    public static void authenticateForDecrypt(
            FragmentActivity activity,
            String           title,
            String           subtitle,
            byte[]           iv,
            BiometricCallback callback) {
        try {
            SecretKey key    = KeyStoreManager.getBiometricKey();
            Cipher    cipher = Cipher.getInstance(CryptoConstants.AES_TRANSFORM);
            cipher.init(
                Cipher.DECRYPT_MODE, key,
                new GCMParameterSpec(CryptoConstants.GCM_TAG_BIT, iv)
            );

            showPrompt(activity, title, subtitle, cipher, callback);

        } catch (Exception e) {
            callback.onError("Biometric setup failed: " + e.getMessage());
        }
    }

    // ── Show Prompt ───────────────────────────────────────────

    private static void showPrompt(
            FragmentActivity  activity,
            String            title,
            String            subtitle,
            Cipher            cipher,
            BiometricCallback callback) {

        Executor executor = ContextCompat.getMainExecutor(activity);

        BiometricPrompt prompt = new BiometricPrompt(
            activity, executor,
            new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(
                        BiometricPrompt.AuthenticationResult result) {
                    Cipher authenticatedCipher = result
                        .getCryptoObject()
                        .getCipher();
                    callback.onSuccess(authenticatedCipher);
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
                .setNegativeButtonText("Use PIN instead")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
                .build();

        prompt.authenticate(
            info,
            new BiometricPrompt.CryptoObject(cipher)
        );
    }
}