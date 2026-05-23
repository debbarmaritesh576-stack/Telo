package com.telo.app.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(
    tableName = "password_entries",
    foreignKeys = {
        @ForeignKey(
            entity          = CategoryEntity.class,
            parentColumns   = "id",
            childColumns    = "categoryId",
            onDelete        = ForeignKey.SET_NULL
        )
    },
    indices = {
        @Index("categoryId"),
        @Index("title"),
        @Index("isFavorite")
    }
)
public class PasswordEntryEntity {

    @PrimaryKey
    @NonNull
    public String  id;
    public String  title;
    public String  username;
    public String  email;
    public String  encryptedPassword;  // AES-GCM encrypted
    public String  url;
    public String  encryptedNotes;     // AES-GCM encrypted
    public String  categoryId;
    public String  groupId;
    public boolean isFavorite;
    public long    createdAt;
    public long    updatedAt;
    public long    passwordChangedAt;
    public boolean hasTotp;
    public String  totpId;
}