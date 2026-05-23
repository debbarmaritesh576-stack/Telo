package com.telo.app.importers;  
  
import android.content.Context;  
import android.net.Uri;  
import com.google.gson.Gson;  
import com.google.gson.JsonArray;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonObject;  
import com.telo.app.otp.OTPAlgorithm;  
import com.telo.app.otp.OTPEntry;  
import com.telo.app.otp.OTPType;  
import java.util.ArrayList;  
import java.util.List;  
  
public class AegisImporter extends BaseImporter {  
  
    private final Gson gson = new Gson();  
  
    public AegisImporter(Context context) {  
        super(context);  
    }  
  
    @Override  
    public String getImporterName() {  
        return "Aegis Authenticator";  
    }  
  
    @Override  
    public ImportResult importFromUri(Uri uri) {  
        try {  
            String data = readFromUri(uri);  
            return importFromString(data);  
        } catch (Exception e) {  
            return ImportResult.failed("File read error: " + e.getMessage());  
        }  
    }  
  
    @Override  
    public ImportResult importFromString(String data) {  
        try {  
            JsonObject root = gson.fromJson(data, JsonObject.class);  
  
            // Check version  
            if (!root.has("version")) {  
                return ImportResult.failed("Invalid Aegis backup format");  
            }  
  
            JsonObject db = root.getAsJsonObject("db");  
            if (db == null) {  
                return ImportResult.failed("No database found in backup");  
            }  
  
            JsonArray entries = db.getAsJsonArray("entries");  
            if (entries == null) {  
                return ImportResult.failed("No entries found");  
            }  
  
            List<OTPEntry> otpEntries = new ArrayList<>();  
            int failCount = 0;  
  
            for (JsonElement element : entries) {  
                try {  
                    OTPEntry entry = parseEntry(element.getAsJsonObject());  
                    if (entry != null) otpEntries.add(entry);  
                    else failCount++;  
                } catch (Exception e) {  
                    failCount++;  
                }  
            }  
  
            if (otpEntries.isEmpty()) {  
                return ImportResult.failed("No valid entries found");  
            }  
  
            return failCount > 0  
                ? ImportResult.partial(otpEntries, failCount)  
                : ImportResult.success(otpEntries);  
  
        } catch (Exception e) {  
            return ImportResult.failed("Parse error: " + e.getMessage());  
        }  
    }  
  
    private OTPEntry parseEntry(JsonObject obj) {  
        OTPEntry entry = new OTPEntry();  
  
        if (obj.has("name")) {  
            entry.setName(obj.get("name").getAsString());  
        }  
        if (obj.has("issuer")) {  
            entry.setIssuer(obj.get("issuer").getAsString());  
        }  
        if (obj.has("icon")) {  
            entry.setIconName(obj.get("icon").getAsString());  
        }  
  
        // Type  
        if (obj.has("type")) {  
            String type = obj.get("type").getAsString();  
            switch (type.toLowerCase()) {  
                case "hotp":  entry.setType(OTPType.HOTP);  break;  
                case "steam": entry.setType(OTPType.STEAM); break;  
                default:      entry.setType(OTPType.TOTP);  break;  
            }  
        }  
  
        // Info object  
        if (obj.has("info")) {  
            JsonObject info = obj.getAsJsonObject("info");  
  
            if (info.has("secret")) {  
                entry.setSecret(info.get("secret").getAsString());  
            }  
            if (info.has("digits")) {  
                entry.setDigits(info.get("digits").getAsInt());  
            }  
            if (info.has("period")) {  
                entry.setPeriod(info.get("period").getAsLong());  
            }  
            if (info.has("counter")) {  
                entry.setCounter(info.get("counter").getAsLong());  
            }  
            if (info.has("algo")) {  
                String algo = info.get("algo").getAsString();  
                switch (algo.toUpperCase()) {  
                    case "SHA256": entry.setAlgorithm(OTPAlgorithm.SHA256); break;  
                    case "SHA512": entry.setAlgorithm(OTPAlgorithm.SHA512); break;  
                    default:       entry.setAlgorithm(OTPAlgorithm.SHA1);   break;  
                }  
            }  
        }  
  
        return entry.getSecret() != null ? entry : null;  
    }  
}