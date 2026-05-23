package com.telo.app.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "password_entries")
public class PasswordEntryEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String username;
    public String email;
    public String password;
    public String url;
    public String notes;
    public String categoryId;
    public String groupId;
    public boolean isFavorite;
    public long   createdAt;
    public long   updatedAt;
    public long   passwordChangedAt;
    public boolean hasTotp;
    public String totpId;
}