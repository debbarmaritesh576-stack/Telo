package com.telo.app.cards;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CardEntity card);

    @Update
    void update(CardEntity card);

    @Delete
    void delete(CardEntity card);

    @Query("DELETE FROM cards WHERE id = :id")
    void deleteById(String id);

    @Query("DELETE FROM cards")
    void deleteAll();

    @Query("SELECT * FROM cards ORDER BY isFavorite DESC, createdAt DESC")
    LiveData<List<CardEntity>> getAll();

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    CardEntity getById(String id);

    @Query("SELECT * FROM cards WHERE isFavorite = 1")
    LiveData<List<CardEntity>> getFavorites();

    @Query("SELECT * FROM cards WHERE categoryId = :categoryId")
    LiveData<List<CardEntity>> getByCategory(String categoryId);

    @Query("SELECT COUNT(*) FROM cards")
    int getCount();
}