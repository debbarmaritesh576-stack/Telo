package com.telo.app.notes;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface SecureNoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SecureNoteEntity note);

    @Update
    void update(SecureNoteEntity note);

    @Delete
    void delete(SecureNoteEntity note);

    @Query("DELETE FROM secure_notes WHERE id = :id")
    void deleteById(String id);

    @Query("DELETE FROM secure_notes")
    void deleteAll();

    @Query("SELECT * FROM secure_notes ORDER BY isPinned DESC, updatedAt DESC")
    LiveData<List<SecureNoteEntity>> getAll();

    @Query("SELECT * FROM secure_notes WHERE id = :id LIMIT 1")
    SecureNoteEntity getById(String id);

    @Query("SELECT * FROM secure_notes WHERE isFavorite = 1")
    LiveData<List<SecureNoteEntity>> getFavorites();

    @Query("SELECT * FROM secure_notes WHERE " +
           "LOWER(title) LIKE '%' || LOWER(:query) || '%' OR " +
           "LOWER(encryptedContent) LIKE '%' || LOWER(:query) || '%'")
    LiveData<List<SecureNoteEntity>> search(String query);

    @Query("SELECT COUNT(*) FROM secure_notes")
    int getCount();
}