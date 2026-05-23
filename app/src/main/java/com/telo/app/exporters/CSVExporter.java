package com.telo.app.exporters;

import android.content.Context;
import android.net.Uri;
import com.telo.app.otp.OTPEntry;
import com.telo.app.passwords.PasswordEntry;
import java.util.List;

public class CSVExporter extends BaseExporter {

    public CSVExporter(Context context) {
        super(context);
    }

    @Override
    public String getExporterName() {
        return "CSV Export";
    }

    @Override
    public ExportResult exportOTP(List<OTPEntry> entries, Uri uri) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("name,issuer,secret,type,algorithm,digits,period,counter\n");

            for (OTPEntry e : entries) {
                sb.append(safe(e.getName())).append(",")
                  .append(safe(e.getIssuer())).append(",")
                  .append(safe(e.getSecret())).append(",")
                  .append(e.getType().name()).append(",")
                  .append(e.getAlgorithm().name()).append(",")
                  .append(e.getDigits()).append(",")
                  .append(e.getPeriod()).append(",")
                  .append(e.getCounter()).append("\n");
            }

            writeToUri(uri, sb.toString());
            return ExportResult.success(uri.getPath(), entries.size());

        } catch (Exception e) {
            return ExportResult.failed("CSV export failed: " + e.getMessage());
        }
    }

    @Override
    public ExportResult exportPasswords(
            List<PasswordEntry> entries, Uri uri) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("title,username,email,password,url,notes,category\n");

            for (PasswordEntry e : entries) {
                sb.append(safe(e.getTitle())).append(",")
                  .append(safe(e.getUsername())).append(",")
                  .append(safe(e.getEmail())).append(",")
                  .append(safe(e.getPassword())).append(",")
                  .append(safe(e.getUrl())).append(",")
                  .append(safe(e.getNotes())).append(",")
                  .append(safe(e.getCategoryId())).append("\n");
            }

            writeToUri(uri, sb.toString());
            return ExportResult.success(uri.getPath(), entries.size());

        } catch (Exception e) {
            return ExportResult.failed("CSV export failed: " + e.getMessage());
        }
    }

    private String safe(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") ||
            value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}