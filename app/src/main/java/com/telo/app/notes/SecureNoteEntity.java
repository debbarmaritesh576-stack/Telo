package com.telo.app.notes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "secure_notes")
public class SecureNoteEntity {

    @PrimaryKey
    @NonNull
    public String  id;
    public String  title;
    public String  encryptedContent;
    public String  categoryId;
    public boolean isFavorite;
    public boolean isPinned;
    public String  colorHex;
    public long    createdAt;
    public long    updatedAt;
}