package com.telo.app.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyStoreManager {

    private static KeyStore keyStore;

    private static KeyStore getKeyStore() throws Exception {
        if (keyStore == null) {
            keyStore = KeyStore.getInstance(
                CryptoConstants.KEYSTORE_PROVIDER
            );
            keyStore.load(null);
        }
        return keyStore;
    }

    // ── Generate Key ──────────────────────────────────────────

    public static SecretKey generateKey(
            String alias,
            boolean requireAuth) throws Exception {
        KeyStore ks = getKeyStore();
        if (ks.containsAlias(alias)) {
            return getKey(alias);
        }

        KeyGenerator kg = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            CryptoConstants.KEYSTORE_PROVIDER
        );

        KeyGenParameterSpec.Builder builder =
            new KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT |
                KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(CryptoConstants.AES_KEY_SIZE)
            .setUserAuthenticationRequired(requireAuth);

        kg.init(builder.build());
        return kg.generateKey();
    }

    // ── Get Key ───────────────────────────────────────────────

    public static SecretKey getKey(String alias) throws Exception {
        KeyStore ks = getKeyStore();
        return (SecretKey) ks.getKey(alias, null);
    }

    // ── Delete Key ────────────────────────────────────────────

    public static void deleteKey(String alias) throws Exception {
        KeyStore ks = getKeyStore();
        if (ks.containsAlias(alias)) {
            ks.deleteEntry(alias);
        }
    }

    // ── Check Key ─────────────────────────────────────────────

    public static boolean keyExists(String alias) throws Exception {
        return getKeyStore().containsAlias(alias);
    }

    // ── Master Key ────────────────────────────────────────────

    public static SecretKey getMasterKey() throws Exception {
        return generateKey(
            CryptoConstants.MASTER_KEY_ALIAS, false
        );
    }

    public static SecretKey getBiometricKey() throws Exception {
        return generateKey(
            CryptoConstants.BIOMETRIC_KEY_ALIAS, true
        );
    }
}