package com.telo.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity category);

    @Update
    void update(CategoryEntity category);

    @Delete
    void delete(CategoryEntity category);

    @Query("DELETE FROM categories WHERE id = :id")
    void deleteById(String id);

    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    LiveData<List<CategoryEntity>> getAll();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    CategoryEntity getById(String id);
}