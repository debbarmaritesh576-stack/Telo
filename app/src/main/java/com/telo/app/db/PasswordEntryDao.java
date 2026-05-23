package com.telo.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface PasswordEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PasswordEntryEntity entry);

    @Update
    void update(PasswordEntryEntity entry);

    @Delete
    void delete(PasswordEntryEntity entry);

    @Query("DELETE FROM password_entries WHERE id = :id")
    void deleteById(String id);

    @Query("DELETE FROM password_entries")
    void deleteAll();

    @Query("SELECT * FROM password_entries ORDER BY isFavorite DESC, title ASC")
    LiveData<List<PasswordEntryEntity>> getAll();

    @Query("SELECT * FROM password_entries WHERE id = :id LIMIT 1")
    PasswordEntryEntity getById(String id);

    @Query("SELECT * FROM password_entries WHERE categoryId = :categoryId ORDER BY title ASC")
    LiveData<List<PasswordEntryEntity>> getByCategory(String categoryId);

    @Query("SELECT * FROM password_entries WHERE isFavorite = 1 ORDER BY title ASC")
    LiveData<List<PasswordEntryEntity>> getFavorites();

    @Query("SELECT * FROM password_entries WHERE " +
           "LOWER(title) LIKE '%' || LOWER(:query) || '%' OR " +
           "LOWER(username) LIKE '%' || LOWER(:query) || '%' OR " +
           "LOWER(url) LIKE '%' || LOWER(:query) || '%' " +
           "ORDER BY title ASC")
    LiveData<List<PasswordEntryEntity>> search(String query);

    @Query("SELECT COUNT(*) FROM password_entries")
    int getCount();
}