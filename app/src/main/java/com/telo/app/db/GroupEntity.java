package com.telo.app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "groups")
public class GroupEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String categoryId;
    public int    sortOrder;
    public long   createdAt;
}