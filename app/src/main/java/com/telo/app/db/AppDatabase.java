package com.telo.app.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        OTPEntryEntity.class,
        PasswordEntryEntity.class,
        CategoryEntity.class,
        GroupEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService DB_EXECUTOR =
        Executors.newFixedThreadPool(4);

    public abstract OTPEntryDao      otpEntryDao();
    public abstract PasswordEntryDao passwordEntryDao();
    public abstract CategoryDao      categoryDao();
    public abstract GroupDao         groupDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "telo_database"
                    )
                    .addCallback(PREPOPULATE_CALLBACK)
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback PREPOPULATE_CALLBACK =
        new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                DB_EXECUTOR.execute(() -> {
                    CategoryDao dao = INSTANCE.categoryDao();

                    // id, name, iconRes (drawable name), colorHex, sortOrder
                    dao.insert(CategoryEntity.create(
                        UUID.randomUUID().toString(),
                        "All",      "ic_category_all",      "#6200EE", 0));
                    dao.insert(CategoryEntity.create(
                        UUID.randomUUID().toString(),
                        "Home",     "ic_category_home",     "#FF5722", 1));
                    dao.insert(CategoryEntity.create(
                        UUID.randomUUID().toString(),
                        "Work",     "ic_category_work",     "#2196F3", 2));
                    dao.insert(CategoryEntity.create(
                        UUID.randomUUID().toString(),
                        "Personal", "ic_category_personal", "#4CAF50", 3));
                    dao.insert(CategoryEntity.create(
                        UUID.randomUUID().toString(),
                        "Banking",  "ic_category_banking",  "#FF9800", 4));
                    dao.insert(CategoryEntity.create(
                        UUID.randomUUID().toString(),
                        "Social",   "ic_category_social",   "#E91E63", 5));
                });
            }
        };
}