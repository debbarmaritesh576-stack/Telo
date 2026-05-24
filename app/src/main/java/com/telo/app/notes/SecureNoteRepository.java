package com.telo.app.notes;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.telo.app.db.AppDatabase;
import java.util.List;
import java.util.stream.Collectors;

public class SecureNoteRepository {

    private final SecureNoteDao dao;

    public SecureNoteRepository(Application app) {
        dao = AppDatabase.getInstance(app).secureNoteDao();
    }

    // ── Read ──────────────────────────────────────────────────

    public LiveData<List<SecureNote>> getAll() {
        return Transformations.map(dao.getAll(), entities ->
            entities.stream().map(e -> {
                try {
                    return NoteEncryption.decrypt(e);
                } catch (Exception ex) {
                    return null;
                }
            })
            .filter(n -> n != null)
            .collect(Collectors.toList())
        );
    }

    public LiveData<List<SecureNote>> getFavorites() {
        return Transformations.map(dao.getFavorites(), entities ->
            entities.stream().map(e -> {
                try {
                    return NoteEncryption.decrypt(e);
                } catch (Exception ex) {
                    return null;
                }
            })
            .filter(n -> n != null)
            .collect(Collectors.toList())
        );
    }

    // ── Write ─────────────────────────────────────────────────

    public void insert(SecureNote note) {
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                dao.insert(NoteEncryption.encrypt(note));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void update(SecureNote note) {
        note.touch();
        AppDatabase.DB_EXECUTOR.execute(() -> {
            try {
                dao.update(NoteEncryption.encrypt(note));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(String id) {
        AppDatabase.DB_EXECUTOR.execute(() ->
            dao.deleteById(id)
        );
    }

    public void deleteAll() {
        AppDatabase.DB_EXECUTOR.execute(dao::deleteAll);
    }
}