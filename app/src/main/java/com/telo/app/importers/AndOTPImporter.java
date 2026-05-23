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
  
public class AndOTPImporter extends BaseImporter {  
  
    private final Gson gson = new Gson();  
  
    public AndOTPImporter(Context context) {  
        super(context);  
    }  
  
    @Override  
    public String getImporterName() {  
        return "andOTP";  
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
            JsonArray array = gson.fromJson(data, JsonArray.class);  
            List<OTPEntry> entries  = new ArrayList<>();  
            int failCount = 0;  
  
            for (JsonElement element : array) {  
                try {  
                    JsonObject obj   = element.getAsJsonObject();  
                    OTPEntry   entry = new OTPEntry();  
  
                    if (obj.has("secret")) {  
                        entry.setSecret(obj.get("secret").getAsString());  
                    }  
                    if (obj.has("issuer")) {  
                        entry.setIssuer(obj.get("issuer").getAsString());  
                    }  
                    if (obj.has("label")) {  
                        entry.setName(obj.get("label").getAsString());  
                    }  
                    if (obj.has("digits")) {  
                        entry.setDigits(obj.get("digits").getAsInt());  
                    }  
                    if (obj.has("period")) {  
                        entry.setPeriod(obj.get("period").getAsLong());  
                    }  
                    if (obj.has("counter")) {  
                        entry.setCounter(obj.get("counter").getAsLong());  
                    }  
                    if (obj.has("type")) {  
                        String type = obj.get("type").getAsString();  
                        entry.setType(type.equals("HOTP")  
                            ? OTPType.HOTP : OTPType.TOTP);  
                    }  
                    if (obj.has("algorithm")) {  
                        String algo = obj.get("algorithm").getAsString();  
                        switch (algo) {  
                            case "SHA256":  
                                entry.setAlgorithm(OTPAlgorithm.SHA256); break;  
                            case "SHA512":  
                                entry.setAlgorithm(OTPAlgorithm.SHA512); break;  
                            default:  
                                entry.setAlgorithm(OTPAlgorithm.SHA1);   break;  
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