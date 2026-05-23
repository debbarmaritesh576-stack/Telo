package com.telo.app.autofill;

import android.content.Context;
import android.content.IntentSender;
import android.service.autofill.Dataset;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveInfo;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;
import com.telo.app.R;
import com.telo.app.passwords.PasswordEntry;
import java.util.List;

public class AutofillHelper {

    public static FillResponse buildFillResponse(
            Context context,
            AutofillParser.ParsedStructure parsed,
            List<PasswordEntry> matches) {

        if (matches == null || matches.isEmpty()) return null;
        if (parsed.usernameId == null && parsed.passwordId == null) return null;

        FillResponse.Builder responseBuilder = new FillResponse.Builder();

        for (PasswordEntry entry : matches) {
            Dataset dataset = buildDataset(context, parsed, entry);
            if (dataset != null) {
                responseBuilder.addDataset(dataset);
            }
        }

        // Save info
        AutofillId[] saveIds = getSaveIds(parsed);
        if (saveIds.length > 0) {
            responseBuilder.setSaveInfo(
                new SaveInfo.Builder(SaveInfo.SAVE_DATA_TYPE_PASSWORD, saveIds)
                    .build()
            );
        }

        return responseBuilder.build();
    }

    private static Dataset buildDataset(
            Context context,
            AutofillParser.ParsedStructure parsed,
            PasswordEntry entry) {

        String label = entry.getTitle() != null
            ? entry.getTitle() : entry.getUsername();

        RemoteViews presentation = new RemoteViews(
            context.getPackageName(),
            R.layout.item_autofill_entry
        );
        presentation.setTextViewText(R.id.tv_autofill_title, label);
        presentation.setTextViewText(
            R.id.tv_autofill_subtitle, entry.getUsername()
        );

        Dataset.Builder datasetBuilder = new Dataset.Builder(presentation);

        if (parsed.usernameId != null && entry.getUsername() != null) {
            datasetBuilder.setValue(
                parsed.usernameId,
                AutofillValue.forText(entry.getUsername()),
                presentation
            );
        }

        if (parsed.emailId != null && entry.getEmail() != null) {
            datasetBuilder.setValue(
                parsed.emailId,
                AutofillValue.forText(entry.getEmail()),
                presentation
            );
        }

        if (parsed.passwordId != null && entry.getPassword() != null) {
            datasetBuilder.setValue(
                parsed.passwordId,
                AutofillValue.forText(entry.getPassword()),
                presentation
            );
        }

        return datasetBuilder.build();
    }

    private static AutofillId[] getSaveIds(AutofillParser.ParsedStructure parsed) {
        java.util.List<AutofillId> ids = new java.util.ArrayList<>();
        if (parsed.usernameId != null) ids.add(parsed.usernameId);
        if (parsed.emailId    != null) ids.add(parsed.emailId);
        if (parsed.passwordId != null) ids.add(parsed.passwordId);
        return ids.toArray(new AutofillId[0]);
    }
}