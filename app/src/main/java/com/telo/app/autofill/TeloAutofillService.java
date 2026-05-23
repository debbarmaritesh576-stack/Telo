package com.telo.app.autofill;

import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;
import android.app.assist.AssistStructure;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.vault.VaultManager;
import java.util.List;

public class TeloAutofillService extends AutofillService {

    private AutofillRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new AutofillRepository(getApplication());
    }

    @Override
    public void onFillRequest(
            FillRequest request,
            CancellationSignal cancellationSignal,
            FillCallback callback) {

        // Vault locked check
        if (!VaultManager.getInstance(this).isUnlocked()) {
            callback.onSuccess(null);
            return;
        }

        List<FillContext> contexts = request.getFillContexts();
        if (contexts == null || contexts.isEmpty()) {
            callback.onSuccess(null);
            return;
        }

        AssistStructure structure = contexts
            .get(contexts.size() - 1)
            .getStructure();

        // Parse structure
        AutofillParser.ParsedStructure parsed =
            AutofillParser.parse(structure);

        // Get package name
        String packageName = structure
            .getActivityComponent()
            .getPackageName();

        // Find matching entries
        List<PasswordEntry> matches = repository.findMatches(
            packageName, parsed.webDomain
        );

        if (matches.isEmpty()) {
            callback.onSuccess(null);
            return;
        }

        // Build response
        FillResponse response = AutofillHelper.buildFillResponse(
            this, parsed, matches
        );

        callback.onSuccess(response);
    }

    @Override
    public void onSaveRequest(
            SaveRequest request,
            SaveCallback callback) {
        // TODO: Save new credentials from autofill
        callback.onSuccess();
    }
}