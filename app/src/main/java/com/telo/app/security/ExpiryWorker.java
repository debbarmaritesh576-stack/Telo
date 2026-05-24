package com.telo.app.security;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.PasswordEntryDao;
import com.telo.app.db.PasswordEntryEntity;
import com.telo.app.passwords.PasswordEntry;
import com.telo.app.passwords.PasswordExpiryChecker;
import com.telo.app.services.NotificationService;
import java.util.ArrayList;
import java.util.List;

public class ExpiryWorker extends Worker {

    public ExpiryWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Get all passwords synchronously
            PasswordEntryDao dao = AppDatabase
                .getInstance(getApplicationContext())
                .passwordEntryDao();

            List<PasswordEntryEntity> entities =
                dao.getAllSync();

            List<PasswordEntry> expiringSoon = new ArrayList<>();

            for (PasswordEntryEntity entity : entities) {
                PasswordEntry entry = new PasswordEntry();
                entry.setId(entity.id);
                entry.setTitle(entity.title);
                entry.setPasswordChangedAt(entity.passwordChangedAt);

                if (PasswordExpiryChecker.check(entry) ==
                        PasswordExpiryChecker.ExpiryStatus.EXPIRING_SOON) {
                    expiringSoon.add(entry);
                }
            }

            if (!expiringSoon.isEmpty()) {
                new NotificationService(getApplicationContext())
                    .showExpiryAlert(expiringSoon.size());
            }

            return Result.success();

        } catch (Exception e) {
            return Result.failure();
        }
    }
}