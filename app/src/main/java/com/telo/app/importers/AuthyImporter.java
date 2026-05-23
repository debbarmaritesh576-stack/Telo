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

public class AuthyImporter extends BaseImporter {

    private final Gson gson = new Gson();

    public AuthyImporter(Context context) {
        super(context);
    }

    @Override
    public String getImporterName() {
        return "Authy";
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
            JsonArray array    = gson.fromJson(data, JsonArray.class);
            List<OTPEntry> entries  = new ArrayList<>();
            int            failCount = 0;

            for (JsonElement element : array) {
                try {
                    JsonObject obj   = element.getAsJsonObject();
                    OTPEntry   entry = new OTPEntry();

                    if (obj.has("name")) {
                        entry.setName(obj.get("name").getAsString());
                    }
                    if (obj.has("issuer")) {
                        entry.setIssuer(obj.get("issuer").getAsString());
                    }
                    if (obj.has("decryptedSeed")) {
                        entry.setSecret(obj.get("decryptedSeed").getAsString());
                    }
                    if (obj.has("digits")) {
                        entry.setDigits(obj.get("digits").getAsInt());
                    } else {
                        entry.setDigits(7); // Authy default is 7
                    }
                    if (obj.has("totp")) {
                        boolean isTotp = obj.get("totp").getAsBoolean();
                        entry.setType(isTotp ? OTPType.TOTP : OTPType.HOTP);
                    }
                    if (obj.has("timeStep")) {
                        entry.setPeriod(obj.get("timeStep").getAsLong());
                    } else {
                        entry.setPeriod(30);
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