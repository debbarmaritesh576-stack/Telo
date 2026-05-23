package com.telo.app.importers;  
  
import android.content.Context;  
import android.net.Uri;  
import com.google.gson.Gson;  
import com.google.gson.JsonArray;  
import com.google.gson.JsonElement;  
import com.google.gson.JsonObject;  
import com.telo.app.otp.OTPEntry;  
import com.telo.app.otp.OTPType;  
import java.util.ArrayList;  
import java.util.List;  
  
public class TwoFASImporter extends BaseImporter {  
  
    private final Gson gson = new Gson();  
  
    public TwoFASImporter(Context context) {  
        super(context);  
    }  
  
    @Override  
    public String getImporterName() {  
        return "2FAS Authenticator";  
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
            JsonObject root     = gson.fromJson(data, JsonObject.class);  
            JsonArray  services = root.getAsJsonArray("services");  
  
            if (services == null) {  
                return ImportResult.failed("No services found in backup");  
            }  
  
            List<OTPEntry> entries  = new ArrayList<>();  
            int            failCount = 0;  
  
            for (JsonElement element : services) {  
                try {  
                    JsonObject obj   = element.getAsJsonObject();  
                    OTPEntry   entry = new OTPEntry();  
  
                    if (obj.has("name")) {  
                        entry.setName(obj.get("name").getAsString());  
                    }  
                    if (obj.has("issuer")) {  
                        entry.setIssuer(obj.get("issuer").getAsString());  
                    }  
  
                    JsonObject otp = obj.getAsJsonObject("otp");  
                    if (otp != null) {  
                        if (otp.has("secret")) {  
                            entry.setSecret(otp.get("secret").getAsString());  
                        }  
                        if (otp.has("digits")) {  
                            entry.setDigits(otp.get("digits").getAsInt());  
                        }  
                        if (otp.has("period")) {  
                            entry.setPeriod(otp.get("period").getAsLong());  
                        }  
                        if (otp.has("tokenType")) {  
                            String type = otp.get("tokenType").getAsString();  
                            entry.setType(type.equals("HOTP")  
                                ? OTPType.HOTP : OTPType.TOTP);  
                        }  
                    }  
  
                    if (entry.getSecret() != null) {  
                        entries.add(entry);  
                    } else {  
                        failCount++;  
                    }  
  
                } catch (Exception e) {  
                    failCount++;  
                }  
            }  
  
            if (entries.isEmpty()) {  
                return ImportResult.failed("No valid entries found");  
            }  
  
            return failCount > 0  
                ? ImportResult.partial(entries, failCount)  
                : ImportResult.success(entries);  
  
        } catch (Exception e) {  
            return ImportResult.failed("Parse error: " + e.getMessage());  
        }  
    }  
}