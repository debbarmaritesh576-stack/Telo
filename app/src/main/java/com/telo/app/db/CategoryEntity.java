package com.telo.app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "categories")
public class CategoryEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String iconRes;    // drawable resource name — ic_category_home etc
    public String colorHex;
    public int    sortOrder;
    public long   createdAt;

    public static CategoryEntity create(
            String id,
            String name,
            String iconRes,
            String colorHex,
            int sortOrder) {
        CategoryEntity entity = new CategoryEntity();
        entity.id        = id;
        entity.name      = name;
        entity.iconRes   = iconRes;
        entity.colorHex  = colorHex;
        entity.sortOrder = sortOrder;
        entity.createdAt = System.currentTimeMillis();
        return entity;
    }
}