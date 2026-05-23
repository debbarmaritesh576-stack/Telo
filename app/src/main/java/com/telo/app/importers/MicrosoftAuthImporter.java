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

public class MicrosoftAuthImporter extends BaseImporter {

    private final Gson gson = new Gson();

    public MicrosoftAuthImporter(Context context) {
        super(context);
    }

    @Override
    public String getImporterName() {
        return "Microsoft Authenticator";
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
            JsonArray  accounts = root.getAsJsonArray("accounts");

            if (accounts == null) {
                return ImportResult.failed("No accounts found");
            }

            List<OTPEntry> entries   = new ArrayList<>();
            int            failCount = 0;

            for (JsonElement element : accounts) {
                try {
                    JsonObject obj   = element.getAsJsonObject();
                    OTPEntry   entry = new OTPEntry();

                    if (obj.has("name")) {
                        entry.setName(obj.get("name").getAsString());
                    }
                    if (obj.has("username")) {
                        entry.setIssuer(obj.get("username").getAsString());
                    }
                    if (obj.has("secret")) {
                        entry.setSecret(obj.get("secret").getAsString());
                    }
                    if (obj.has("digits")) {
                        entry.setDigits(obj.get("digits").getAsInt());
                    }
                    if (obj.has("timeStep")) {
                        entry.setPeriod(obj.get("timeStep").getAsLong());
                    }
                    if (obj.has("type")) {
                        String type = obj.get("type").getAsString();
                        switch (type.toLowerCase()) {
                            case "hotp":
                                entry.setType(OTPType.HOTP); break;
                            default:
                                entry.setType(OTPType.TOTP); break;
                        }
                    }
                    if (obj.has("algorithm")) {
                        String algo = obj.get("algorithm").getAsString();
                        switch (algo.toUpperCase()) {
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