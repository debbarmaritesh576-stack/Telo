package com.telo.app.importers;  
  
import android.content.Context;  
import android.net.Uri;  
import java.io.BufferedReader;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
  
public abstract class BaseImporter {  
  
    protected final Context context;  
  
    public BaseImporter(Context context) {  
        this.context = context;  
    }  
  
    public abstract ImportResult importFromUri(Uri uri);  
    public abstract ImportResult importFromString(String data);  
    public abstract String getImporterName();  
  
    protected String readFromUri(Uri uri) throws Exception {  
        InputStream is = context  
            .getContentResolver()  
            .openInputStream(uri);  
  
        if (is == null) throw new Exception("Cannot open file");  
  
        BufferedReader reader = new BufferedReader(  
            new InputStreamReader(is)  
        );  
        StringBuilder sb = new StringBuilder();  
        String line;  
        while ((line = reader.readLine()) != null) {  
            sb.append(line).append("\n");  
        }  
        reader.close();  
        return sb.toString();  
    }  
  
    protected byte[] readBytesFromUri(Uri uri) throws Exception {  
        InputStream is = context  
            .getContentResolver()  
            .openInputStream(uri);  
  
        if (is == null) throw new Exception("Cannot open file");  
  
        java.io.ByteArrayOutputStream buffer =  
            new java.io.ByteArrayOutputStream();  
        byte[] chunk = new byte[4096];  
        int len;  
        while ((len = is.read(chunk)) != -1) {  
            buffer.write(chunk, 0, len);  
        }  
        is.close();  
        return buffer.toByteArray();  
    }  
}