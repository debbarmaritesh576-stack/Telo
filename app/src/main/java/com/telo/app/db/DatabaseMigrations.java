package com.telo.app.db;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseMigrations {

    // ── V1 → V2 ───────────────────────────────────────────────
    // Added encryption columns

    public static final Migration MIGRATION_1_2 =
        new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase db) {
                // Rename old password column to encrypted
                db.execSQL(
                    "ALTER TABLE password_entries " +
                    "RENAME COLUMN password TO encryptedPassword"
                );
                db.execSQL(
                    "ALTER TABLE password_entries " +
                    "RENAME COLUMN notes TO encryptedNotes"
                );
                // OTP secret encryption
                db.execSQL(
                    "ALTER TABLE otp_entries " +
                    "RENAME COLUMN secret TO encryptedSecret"
                );
                // Add indexes
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS " +
                    "index_password_entries_categoryId " +
                    "ON password_entries(categoryId)"
                );
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS " +
                    "index_otp_entries_categoryId " +
                    "ON otp_entries(categoryId)"
                );
            }
        };

    // ── V2 → V3 ───────────────────────────────────────────────
    // Future migration placeholder

    public static final Migration MIGRATION_2_3 =
        new Migration(2, 3) {
            @Override
            public void migrate(SupportSQLiteDatabase db) {
                // Future changes here
            }
        };
}