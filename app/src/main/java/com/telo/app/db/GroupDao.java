package com.telo.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GroupEntity group);

    @Update
    void update(GroupEntity group);

    @Delete
    void delete(GroupEntity group);

    @Query("DELETE FROM groups WHERE id = :id")
    void deleteById(String id);

    @Query("SELECT * FROM groups ORDER BY sortOrder ASC")
    LiveData<List<GroupEntity>> getAll();

    @Query("SELECT * FROM groups WHERE categoryId = :categoryId ORDER BY sortOrder ASC")
    LiveData<List<GroupEntity>> getByCategory(String categoryId);

    @Query("SELECT * FROM groups WHERE id = :id LIMIT 1")
    GroupEntity getById(String id);
}