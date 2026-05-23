package com.telo.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface OTPEntryDao {

    // ── Insert ────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OTPEntryEntity entry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OTPEntryEntity> entries);

    // ── Update ────────────────────────────────────────────────

    @Update
    void update(OTPEntryEntity entry);

    @Query("UPDATE otp_entries SET counter = :counter WHERE id = :id")
    void updateCounter(String id, long counter);

    @Query("UPDATE otp_entries SET isFavorite = :fav WHERE id = :id")
    void updateFavorite(String id, boolean fav);

    // ── Delete ────────────────────────────────────────────────

    @Delete
    void delete(OTPEntryEntity entry);

    @Query("DELETE FROM otp_entries WHERE id = :id")
    void deleteById(String id);

    @Query("DELETE FROM otp_entries")
    void deleteAll();

    // ── Query ─────────────────────────────────────────────────

    @Query("SELECT * FROM otp_entries ORDER BY isFavorite DESC, name ASC")
    LiveData<List<OTPEntryEntity>> getAll();

    @Query("SELECT * FROM otp_entries WHERE id = :id LIMIT 1")
    OTPEntryEntity getById(String id);

    @Query("SELECT * FROM otp_entries WHERE categoryId = :categoryId ORDER BY name ASC")
    LiveData<List<OTPEntryEntity>> getByCategory(String categoryId);

    @Query("SELECT * FROM otp_entries WHERE isFavorite = 1 ORDER BY name ASC")
    LiveData<List<OTPEntryEntity>> getFavorites();

    @Query("SELECT * FROM otp_entries WHERE " +
           "LOWER(name) LIKE '%' || LOWER(:query) || '%' OR " +
           "LOWER(issuer) LIKE '%' || LOWER(:query) || '%' " +
           "ORDER BY name ASC")
    LiveData<List<OTPEntryEntity>> search(String query);

    @Query("SELECT COUNT(*) FROM otp_entries")
    int getCount();
}