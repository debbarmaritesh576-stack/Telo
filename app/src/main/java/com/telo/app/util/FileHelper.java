package com.telo.app.util;

import android.content.Context;
import android.net.Uri;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileHelper {

    // ── Read ──────────────────────────────────────────────────

    public static String readText(Context context, Uri uri) throws Exception {
        InputStream is = context.getContentResolver().openInputStream(uri);
        if (is == null) throw new Exception("Cannot open file");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static byte[] readBytes(Context context, Uri uri) throws Exception {
        InputStream is = context.getContentResolver().openInputStream(uri);
        if (is == null) throw new Exception("Cannot open file");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int len;
        while ((len = is.read(chunk)) != -1) {
            buffer.write(chunk, 0, len);
        }
        is.close();
        return buffer.toByteArray();
    }

    // ── Write ─────────────────────────────────────────────────

    public static void writeText(
            Context context, Uri uri, String text) throws Exception {
        OutputStream os = context.getContentResolver().openOutputStream(uri);
        if (os == null) throw new Exception("Cannot write file");
        os.write(text.getBytes("UTF-8"));
        os.flush();
        os.close();
    }

    public static void writeBytes(
            Context context, Uri uri, byte[] data) throws Exception {
        OutputStream os = context.getContentResolver().openOutputStream(uri);
        if (os == null) throw new Exception("Cannot write file");
        os.write(data);
        os.flush();
        os.close();
    }

    // ── Filename ──────────────────────────────────────────────

    public static String generateBackupName(String prefix, String ext) {
        String date = new SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(new Date());
        return prefix + "_" + date + "." + ext;
    }

    public static String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1).toLowerCase() : "";
    }
}