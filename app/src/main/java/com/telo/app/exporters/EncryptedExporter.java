package com.telo.app.exporters;

import android.content.Context;
import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telo.app.otp.OTPEntry;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.vault.VaultEncryption;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncryptedExporter extends BaseExporter {

    private final Gson   gson;
    private final char[] backupPassword;

    public EncryptedExporter(Context context, char[] backupPassword) {
        super(context);
        this.gson           = new GsonBuilder().setPrettyPrinting().create();
        this.backupPassword = backupPassword;
    }

    @Override
    public String getExporterName() {
        return "Telo Encrypted Backup";
    }

    @Override
    public ExportResult exportOTP(List<OTPEntry> entries, Uri uri) {
        try {
            Map<String, Object> backup = new HashMap<>();
            backup.put("version",  2);
            backup.put("type",     "otp");
            backup.put("entries",  entries);
            backup.put("exportedAt", System.currentTimeMillis());

            String json  = gson.toJson(backup);
            byte[] salt  = VaultEncryption.generateSalt();
            byte[] key   = VaultEncryption.deriveKeyFromPassword(
                backupPassword, salt
            );
            byte[] iv        = VaultEncryption.generateIv();
            byte[] encrypted = VaultEncryption.encrypt(
                json.getBytes(StandardCharsets.UTF_8), key, iv
            );

            // Format: salt(32) + iv(12) + encrypted
            byte[] output = new byte[salt.length + iv.length + encrypted.length];
            System.arraycopy(salt,      0, output, 0,                         salt.length);
            System.arraycopy(iv,        0, output, salt.length,               iv.length);
            System.arraycopy(encrypted, 0, output, salt.length + iv.length,   encrypted.length);

            writeToUri(uri, output);
            return ExportResult.success(uri.getPath(), entries.size());

        } catch (Exception e) {
            return ExportResult.failed("Export failed: " + e.getMessage());
        }
    }

    @Override
    public ExportResult exportPasswords(
            List<PasswordEntry> entries, Uri uri) {
        try {
            Map<String, Object> backup = new HashMap<>();
            backup.put("version",    2);
            backup.put("type",       "passwords");
            backup.put("entries",    entries);
            backup.put("exportedAt", System.currentTimeMillis());

            String json      = gson.toJson(backup);
            byte[] salt      = VaultEncryption.generateSalt();
            byte[] key       = VaultEncryption.deriveKeyFromPassword(
                backupPassword, salt
            );
            byte[] iv        = VaultEncryption.generateIv();
            byte[] encrypted = VaultEncryption.encrypt(
                json.getBytes(StandardCharsets.UTF_8), key, iv
            );

            byte[] output = new byte[salt.length + iv.length + encrypted.length];
            System.arraycopy(salt,      0, output, 0,                       salt.length);
            System.arraycopy(iv,        0, output, salt.length,             iv.length);
            System.arraycopy(encrypted, 0, output, salt.length + iv.length, encrypted.length);

            writeToUri(uri, output);
            return ExportResult.success(uri.getPath(), entries.size());

        } catch (Exception e) {
            return ExportResult.failed("Export failed: " + e.getMessage());
        }
    }
}