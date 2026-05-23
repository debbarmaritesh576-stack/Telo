package com.telo.app.crypto;

import com.telo.app.passwords.PasswordEntry;
import com.telo.app.notes.SecureNote;

public class VaultCipher {

    // ── Password Encryption ───────────────────────────────────

    public static PasswordEntry encryptPassword(PasswordEntry entry)
            throws Exception {
        if (entry.getPassword() != null) {
            entry.setPassword(
                CryptoManager.encryptToBase64(entry.getPassword())
            );
        }
        if (entry.getNotes() != null) {
            entry.setNotes(
                CryptoManager.encryptToBase64(entry.getNotes())
            );
        }
        return entry;
    }

    public static PasswordEntry decryptPassword(PasswordEntry entry)
            throws Exception {
        if (entry.getPassword() != null &&
            CryptoManager.isEncrypted(entry.getPassword())) {
            entry.setPassword(
                CryptoManager.decryptFromBase64(entry.getPassword())
            );
        }
        if (entry.getNotes() != null &&
            CryptoManager.isEncrypted(entry.getNotes())) {
            entry.setNotes(
                CryptoManager.decryptFromBase64(entry.getNotes())
            );
        }
        return entry;
    }

    // ── Note Encryption ───────────────────────────────────────

    public static SecureNote encryptNote(SecureNote note)
            throws Exception {
        if (note.getContent() != null) {
            note.setContent(
                CryptoManager.encryptToBase64(note.getContent())
            );
        }
        return note;
    }

    public static SecureNote decryptNote(SecureNote note)
            throws Exception {
        if (note.getContent() != null &&
            CryptoManager.isEncrypted(note.getContent())) {
            note.setContent(
                CryptoManager.decryptFromBase64(note.getContent())
            );
        }
        return note;
    }

    // ── OTP Secret Encryption ─────────────────────────────────

    public static String encryptSecret(String secret) throws Exception {
        return CryptoManager.encryptToBase64(secret);
    }

    public static String decryptSecret(String encryptedSecret)
            throws Exception {
        return CryptoManager.decryptFromBase64(encryptedSecret);
    }
}