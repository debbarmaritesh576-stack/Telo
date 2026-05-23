package com.telo.app.exporters;

import android.content.Context;
import android.net.Uri;
import com.telo.app.otp.OTPEntry;
import com.telo.app.passwords.PasswordEntry;
import java.io.OutputStream;
import java.util.List;

public abstract class BaseExporter {

    protected final Context context;

    public BaseExporter(Context context) {
        this.context = context;
    }

    public abstract ExportResult exportOTP(
        List<OTPEntry> entries, Uri uri);

    public abstract ExportResult exportPasswords(
        List<PasswordEntry> entries, Uri uri);

    public abstract String getExporterName();

    protected void writeToUri(Uri uri, byte[] data) throws Exception {
        OutputStream os = context
            .getContentResolver()
            .openOutputStream(uri);
        if (os == null) throw new Exception("Cannot open output stream");
        os.write(data);
        os.flush();
        os.close();
    }

    protected void writeToUri(Uri uri, String data) throws Exception {
        writeToUri(uri, data.getBytes("UTF-8"));
    }
}