package com.telo.app.exporters;

import android.content.Context;
import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telo.app.otp.OTPEntry;
import com.telo.app.passwords.PasswordEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlainTextExporter extends BaseExporter {

    private final Gson gson;

    public PlainTextExporter(Context context) {
        super(context);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public String getExporterName() {
        return "Plain Text JSON";
    }

    @Override
    public ExportResult exportOTP(List<OTPEntry> entries, Uri uri) {
        try {
            Map<String, Object> backup = new HashMap<>();
            backup.put("version",    1);
            backup.put("type",       "otp");
            backup.put("entries",    entries);
            backup.put("exportedAt", System.currentTimeMillis());

            String json = gson.toJson(backup);
            writeToUri(uri, json);
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
            backup.put("version",    1);
            backup.put("type",       "passwords");
            backup.put("entries",    entries);
            backup.put("exportedAt", System.currentTimeMillis());

            String json = gson.toJson(backup);
            writeToUri(uri, json);
            return ExportResult.success(uri.getPath(), entries.size());

        } catch (Exception e) {
            return ExportResult.failed("Export failed: " + e.getMessage());
        }
    }
}