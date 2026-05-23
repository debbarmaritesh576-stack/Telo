package com.telo.app.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import java.nio.charset.StandardCharsets;

public class PinManager {

    private static final String PREFS_NAME  = "telo_pin";
    private static final String KEY_PIN     = "pin_hash";
    private static final String KEY_SALT    = "pin_salt";
    private static final String KEY_ENABLED = "pin_enabled";

    private final SharedPreferences prefs;

    public PinManager(Context context) {
        this.prefs = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        );
    }

    // ── Setup PIN ─────────────────────────────────────────────

    public void setPin(String pin) throws Exception {
        validatePin(pin);
        byte[] salt    = KeyDerivation.generateSalt();
        byte[] derived = KeyDerivation.deriveKeyDefault(
            pin.toCharArray(), salt
        );
        String hashHex = HashManager.bytesToHex(derived);
        String saltHex = HashManager.bytesToHex(salt);

        prefs.edit()
             .putString(KEY_PIN,     hashHex)
             .putString(KEY_SALT,    saltHex)
             .putBoolean(KEY_ENABLED, true)
             .apply();
    }

    // ── Verify PIN ────────────────────────────────────────────

    public boolean verifyPin(String pin) throws Exception {
        if (!isPinEnabled()) return false;

        String saltHex    = prefs.getString(KEY_SALT, null);
        String storedHash = prefs.getString(KEY_PIN,  null);

        if (saltHex == null || storedHash == null) return false;

        byte[] salt    = HashManager.hexToBytes(saltHex);
        byte[] derived = KeyDerivation.deriveKeyDefault(
            pin.toCharArray(), salt
        );
        String inputHash = HashManager.bytesToHex(derived);

        return HashManager.safeEquals(
            storedHash.getBytes(StandardCharsets.UTF_8),
            inputHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ── Remove PIN ────────────────────────────────────────────

    public void removePin() {
        prefs.edit()
             .remove(KEY_PIN)
             .remove(KEY_SALT)
             .putBoolean(KEY_ENABLED, false)
             .apply();
    }

    public boolean isPinEnabled() {
        return prefs.getBoolean(KEY_ENABLED, false);
    }

    // ── Validation ────────────────────────────────────────────

    private void validatePin(String pin) throws Exception {
        if (pin == null || pin.length() < CryptoConstants.PIN_MIN_LENGTH) {
            throw new Exception("PIN too short — min "
                + CryptoConstants.PIN_MIN_LENGTH + " digits");
        }
        if (pin.length() > CryptoConstants.PIN_MAX_LENGTH) {
            throw new Exception("PIN too long — max "
                + CryptoConstants.PIN_MAX_LENGTH + " digits");
        }
        if (!pin.matches("[0-9]+")) {
            throw new Exception("PIN must be numeric");
        }
    }
}