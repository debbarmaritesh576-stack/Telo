package com.telo.app.vault;

import android.content.Context;
import android.net.Uri;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VaultBackup {

    private static final String BACKUP_PREFIX = "telo_backup_";
    private static final String BACKUP_EXT    = ".telo";

    // ── Export ────────────────────────────────────────────────

    public static void exportToUri(
            Context context,
            byte[] encryptedData,
            Uri uri) throws IOException {
        try (OutputStream os = context.getContentResolver().openOutputStream(uri)) {
            if (os == null) throw new IOException("Cannot open output stream");
            os.write(encryptedData);
            os.flush();
        }
    }

    public static void exportToFile(
            byte[] encryptedData,
            File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(encryptedData);
            fos.flush();
        }
    }

    // ── Import ────────────────────────────────────────────────

    public static byte[] importFromUri(
            Context context,
            Uri uri) throws IOException {
        try (InputStream is = context.getContentResolver().openInputStream(uri)) {
            if (is == null) throw new IOException("Cannot open input stream");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int    len;
            while ((len = is.read(chunk)) != -1) {
                buffer.write(chunk, 0, len);
            }
            return buffer.toByteArray();
        }
    }

    public static byte[] importFromFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int    len;
            while ((len = fis.read(chunk)) != -1) {
                buffer.write(chunk, 0, len);
            }
            return buffer.toByteArray();
        }
    }

    // ── Helpers ───────────────────────────────────────────────

    public static String generateBackupFileName() {
        String date = new SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(new Date());
        return BACKUP_PREFIX + date + BACKUP_EXT;
    }

    public static boolean isValidBackupFile(String fileName) {
        return fileName != null && fileName.endsWith(BACKUP_EXT);
    }
}