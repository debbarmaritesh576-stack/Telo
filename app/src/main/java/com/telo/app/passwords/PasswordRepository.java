package com.telo.app.passwords;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.telo.app.db.AppDatabase;
import com.telo.app.db.PasswordEntryDao;
import com.telo.app.db.PasswordEntryEntity;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PasswordRepository {

    private final PasswordEntryDao dao;

    public PasswordRepository(Application app) {
        dao = AppDatabase.getInstance(app).passwordEntryDao();
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<PasswordEntry>> getAll() {
        return Transformations.map(dao.getAll(), entities ->
            entities.stream()
                .map(this::toEntry)
                .collect(Collectors.toList())
        );
    }

    public LiveData<List<PasswordEntry>> getByCategory(String categoryId) {
        return Transformations.map(dao.getByCategory(categoryId), entities ->
            entities.stream()
                .map(this::toEntry)
                .collect(Collectors.toList())
        );
    }

    public LiveData<List<PasswordEntry>> getFavorites() {
        return Transformations.map(dao.getFavorites(), entities ->
            entities.stream()
                .map(this::toEntry)
                .collect(Collectors.toList())
        );
    }

    public LiveData<List<PasswordEntry>> search(String query) {
        return Transformations.map(dao.search(query), entities ->
            entities.stream()
                .map(this::toEntry)
                .collect(Collectors.toList())
        );
    }

    // ── Write ─────────────────────────────────────────────────

    public void insert(PasswordEntry entry) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.insert(toEntity(entry))
        );
    }

    public void update(PasswordEntry entry) {
        entry.touch();
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.update(toEntity(entry))
        );
    }

    public void delete(String id) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.deleteById(id)
        );
    }

    public void deleteAll() {
        AppDatabase.DB_EXECUTOR.execute(dao::deleteAll);
    }

    // ── Mapping ───────────────────────────────────────────────

    private PasswordEntry toEntry(PasswordEntryEntity e) {
        PasswordEntry entry = new PasswordEntry();
        entry.setId(e.id);
        entry.setTitle(e.title);
        entry.setUsername(e.username);
        entry.setEmail(e.email);
        entry.setPassword(e.password);
        entry.setUrl(e.url);
        entry.setNotes(e.notes);
        entry.setCategoryId(e.categoryId);
        entry.setGroupId(e.groupId);
        entry.setFavorite(e.isFavorite);
        entry.setCreatedAt(e.createdAt);
        entry.setUpdatedAt(e.updatedAt);
        entry.setPasswordChangedAt(e.passwordChangedAt);
        entry.setHasTotp(e.hasTotp);
        entry.setTotpId(e.totpId);
        return entry;
    }

    private PasswordEntryEntity toEntity(PasswordEntry e) {
        PasswordEntryEntity entity = new PasswordEntryEntity();
        entity.id                = e.getId();
        entity.title             = e.getTitle();
        entity.username          = e.getUsername();
        entity.email             = e.getEmail();
        entity.password          = e.getPassword();
        entity.url               = e.getUrl();
        entity.notes             = e.getNotes();
        entity.categoryId        = e.getCategoryId();
        entity.groupId           = e.getGroupId();
        entity.isFavorite        = e.isFavorite();
        entity.createdAt         = e.getCreatedAt();
        entity.updatedAt         = e.getUpdatedAt();
        entity.passwordChangedAt = e.getPasswordChangedAt();
        entity.hasTotp           = e.isHasTotp();
        entity.totpId            = e.getTotpId();
        return entity;
    }
}