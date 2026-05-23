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
import com.telo.app.otp.Base32;  
import java.util.ArrayList;  
import java.util.List;  
  
public class FreeOTPImporter extends BaseImporter {  
  
    private final Gson gson = new Gson();  
  
    public FreeOTPImporter(Context context) {  
        super(context);  
    }  
  
    @Override  
    public String getImporterName() {  
        return "FreeOTP";  
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
            JsonObject root    = gson.fromJson(data, JsonObject.class);  
            JsonArray  tokens  = root.getAsJsonArray("tokens");  
  
            if (tokens == null) {  
                return ImportResult.failed("No tokens found");  
            }  
  
            List<OTPEntry> entries  = new ArrayList<>();  
            int            failCount = 0;  
  
            for (JsonElement element : tokens) {  
                try {  
                    JsonObject obj   = element.getAsJsonObject();  
                    OTPEntry   entry = new OTPEntry();  
  
                    if (obj.has("issuerExt")) {  
                        entry.setIssuer(obj.get("issuerExt").getAsString());  
                    }  
                    if (obj.has("label")) {  
                        entry.setName(obj.get("label").getAsString());  
                    }  
                    if (obj.has("secret")) {  
                        JsonArray secretArray = obj.getAsJsonArray("secret");  
                        byte[] secretBytes = new byte[secretArray.size()];  
                        for (int i = 0; i < secretArray.size(); i++) {  
                            secretBytes[i] = (byte) secretArray.get(i).getAsInt();  
                        }  
                        entry.setSecret(Base32.encode(secretBytes));  
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
                    if (obj.has("algo")) {  
                        String algo = obj.get("algo").getAsString();  
                        switch (algo) {  
                            case "SHA256":  
                                entry.setAlgorithm(OTPAlgorithm.SHA256); break;  
                            case "SHA512":  
                                entry.setAlgorithm(OTPAlgorithm.SHA512); break;  
                            default:  
                                entry.setAlgorithm(OTPAlgorithm.SHA1); break;  
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